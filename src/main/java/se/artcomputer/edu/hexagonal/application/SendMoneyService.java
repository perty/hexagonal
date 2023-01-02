package se.artcomputer.edu.hexagonal.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.artcomputer.edu.hexagonal.application.port.in.SendMoneyCommand;
import se.artcomputer.edu.hexagonal.application.port.in.SendMoneyUseCase;
import se.artcomputer.edu.hexagonal.application.port.out.AccountLock;
import se.artcomputer.edu.hexagonal.application.port.out.LoadAccountPort;
import se.artcomputer.edu.hexagonal.application.port.out.UpdateAccountStatePort;
import se.artcomputer.edu.hexagonal.domain.Account;

import java.time.LocalDateTime;

@Service
public class SendMoneyService implements SendMoneyUseCase {

	private final LoadAccountPort loadAccountPort;
	private final AccountLock accountLock;
	private final UpdateAccountStatePort updateAccountStatePort;
	private final MoneyTransferProperties moneyTransferProperties;

	@Autowired
	public SendMoneyService(LoadAccountPort loadAccountPort,
							AccountLock accountLock,
							UpdateAccountStatePort updateAccountStatePort,
							MoneyTransferProperties moneyTransferProperties) {
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
				command.sourceAccountId(),
				baselineDate);

		Account targetAccount = loadAccountPort.loadAccount(
				command.targetAccountId(),
				baselineDate);

		Account.AccountId sourceAccountId = sourceAccount.getId()
				.orElseThrow(() -> new IllegalStateException("expected source account ID not to be empty"));
		Account.AccountId targetAccountId = targetAccount.getId()
				.orElseThrow(() -> new IllegalStateException("expected target account ID not to be empty"));

		accountLock.lockAccount(sourceAccountId);
		if (!sourceAccount.withdraw(command.money(), targetAccountId)) {
			accountLock.releaseAccount(sourceAccountId);
			return false;
		}

		accountLock.lockAccount(targetAccountId);
		if (!targetAccount.deposit(command.money(), sourceAccountId)) {
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
		if(command.money().isGreaterThan(moneyTransferProperties.getMaximumTransferThreshold())){
			throw new ThresholdExceededException(moneyTransferProperties.getMaximumTransferThreshold(), command.money());
		}
	}

}