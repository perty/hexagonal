package se.artcomputer.edu.hexagonal.application.port.in;

import se.artcomputer.edu.hexagonal.domain.Account;
import se.artcomputer.edu.hexagonal.domain.Money;

public class SendMoneyCommand {

    private final Account.AccountId sourceAccountId;
    private final Account.AccountId targetAccountId;
    private final Money money;

    public SendMoneyCommand(
            Account.AccountId sourceAccountId,
            Account.AccountId targetAccountId,
            Money money) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.money = money;
    }

    public Account.AccountId getSourceAccountId() {
        return sourceAccountId;
    }

    public Account.AccountId getTargetAccountId() {
        return targetAccountId;
    }

    public Money getMoney() {
        return money;
    }
}