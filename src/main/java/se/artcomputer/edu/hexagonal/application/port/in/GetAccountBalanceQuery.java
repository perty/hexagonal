package se.artcomputer.edu.hexagonal.application.port.in;

import se.artcomputer.edu.hexagonal.domain.Account;
import se.artcomputer.edu.hexagonal.domain.Money;

public interface GetAccountBalanceQuery {

	Money getAccountBalance(Account.AccountId accountId);

}