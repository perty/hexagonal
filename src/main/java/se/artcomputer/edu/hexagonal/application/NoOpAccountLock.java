package se.artcomputer.edu.hexagonal.application;

import org.springframework.stereotype.Component;
import se.artcomputer.edu.hexagonal.application.port.out.AccountLock;
import se.artcomputer.edu.hexagonal.domain.Account;

@Component
class NoOpAccountLock implements AccountLock {

	@Override
	public void lockAccount(Account.AccountId accountId) {
		// do nothing
	}

	@Override
	public void releaseAccount(Account.AccountId accountId) {
		// do nothing
	}

}