package se.artcomputer.edu.hexagonal.application;

import se.artcomputer.edu.hexagonal.application.port.in.SendMoneyCommand;
import se.artcomputer.edu.hexagonal.application.port.in.SendMoneyUseCase;
import se.artcomputer.edu.hexagonal.application.port.out.AccountLock;
import se.artcomputer.edu.hexagonal.application.port.out.LoadAccountPort;
import se.artcomputer.edu.hexagonal.application.port.out.UpdateAccountStatePort;
import se.artcomputer.edu.hexagonal.domain.Account;

import java.time.LocalDateTime;

public class SendMoneyService implements SendMoneyUseCase {

	private final LoadAccountPort loadAccountPort;
	private final AccountLock accountLock;
	private final UpdateAccountStatePort updateAccountStatePort;
	private final MoneyTransferProperties moneyTransferProperties;

	public SendMoneyService(LoadAccountPort loadAccountPort, AccountLock accountLock, UpdateAccountStatePort updateAccountStatePort, MoneyTransferProperties moneyTransferProperties) {
		this.loadAccountPort = loadAccountPort;
		this.accountLock = accountLock;
		this.updateAccountStatePort = updateAccountStatePort;
		this.moneyTransferProperties = moneyTransferProperties;
	}

	@Override
	public boolean sendMoney(SendMoneyCommand command) {

		checkThreshold(command);

		LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);

		Account sourceAccount = loadAccountPort.loadAccount(
				command.getSourceAccountId(),
				baselineDate);

		Account targetAccount = loadAccountPort.loadAccount(
				command.getTargetAccountId(),
				baselineDate);

		Account.AccountId sourceAccountId = sourceAccount.getId()
				.orElseThrow(() -> new IllegalStateException("expected source account ID not to be empty"));
		Account.AccountId targetAccountId = targetAccount.getId()
				.orElseThrow(() -> new IllegalStateException("expected target account ID not to be empty"));

		accountLock.lockAccount(sourceAccountId);
		if (!sourceAccount.withdraw(command.getMoney(), targetAccountId)) {
			accountLock.releaseAccount(sourceAccountId);
			return false;
		}

		accountLock.lockAccount(targetAccountId);
		if (!targetAccount.deposit(command.getMoney(), sourceAccountId)) {
			accountLock.releaseAccount(sourceAccountId);
			accountLock.releaseAccount(targetAccountId);
			return false;
		}

		updateAccountStatePort.updateActivities(sourceAccount);
		updateAccountStatePort.updateActivities(targetAccount);

		accountLock.releaseAccount(sourceAccountId);
		accountLock.releaseAccount(targetAccountId);
		return true;
	}

	private void checkThreshold(SendMoneyCommand command) {
		if(command.getMoney().isGreaterThan(moneyTransferProperties.getMaximumTransferThreshold())){
			throw new ThresholdExceededException(moneyTransferProperties.getMaximumTransferThreshold(), command.getMoney());
		}
	}

}