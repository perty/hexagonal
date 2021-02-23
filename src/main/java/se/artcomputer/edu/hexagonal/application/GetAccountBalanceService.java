package se.artcomputer.edu.hexagonal.application;

import se.artcomputer.edu.hexagonal.application.port.in.GetAccountBalanceQuery;
import se.artcomputer.edu.hexagonal.application.port.out.LoadAccountPort;
import se.artcomputer.edu.hexagonal.domain.Account;
import se.artcomputer.edu.hexagonal.domain.Money;

import java.time.LocalDateTime;

class GetAccountBalanceService implements GetAccountBalanceQuery {

	private final LoadAccountPort loadAccountPort;

	GetAccountBalanceService(LoadAccountPort loadAccountPort) {
		this.loadAccountPort = loadAccountPort;
	}

	@Override
	public Money getAccountBalance(Account.AccountId accountId) {
		return loadAccountPort.loadAccount(accountId, LocalDateTime.now())
				.calculateBalance();
	}
}