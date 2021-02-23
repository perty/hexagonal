package se.artcomputer.edu.hexagonal.application.port.out;

import se.artcomputer.edu.hexagonal.domain.Account;

public interface UpdateAccountStatePort {

	void updateActivities(Account account);

}