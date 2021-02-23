package se.artcomputer.edu.hexagonal.application.port.out;

import se.artcomputer.edu.hexagonal.domain.Account;

public interface AccountLock {

	void lockAccount(Account.AccountId accountId);

	void releaseAccount(Account.AccountId accountId);

}