package se.artcomputer.edu.hexagonal.application.port.in;

import se.artcomputer.edu.hexagonal.domain.Account;
import se.artcomputer.edu.hexagonal.domain.Money;

public record SendMoneyCommand(Account.AccountId sourceAccountId, Account.AccountId targetAccountId, Money money) {

}