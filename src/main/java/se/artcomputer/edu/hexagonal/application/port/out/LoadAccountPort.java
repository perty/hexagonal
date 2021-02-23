package se.artcomputer.edu.hexagonal.application.port.out;

import se.artcomputer.edu.hexagonal.domain.Account;

import java.time.LocalDateTime;

public interface LoadAccountPort {

	Account loadAccount(Account.AccountId accountId, LocalDateTime baselineDate);
}