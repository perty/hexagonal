package se.artcomputer.edu.hexagonal.domain;

import java.time.LocalDateTime;

/**
 * A money transfer activity between {@link Account}s.
 */

public class Activity {

    private ActivityId id;

    /**
     * The account that owns this activity.
     */
    private final Account.AccountId ownerAccountId;

    /**
     * The debited account.
     */
    private final Account.AccountId sourceAccountId;

    /**
     * The credited account.
     */
    private final Account.AccountId targetAccountId;

    /**
     * The timestamp of the activity.
     */
    private final LocalDateTime timestamp;

    /**
     * The money that was transferred between the accounts.
     */
    private final Money money;

    public Activity(
            Account.AccountId ownerAccountId,
            Account.AccountId sourceAccountId,
            Account.AccountId targetAccountId,
            LocalDateTime timestamp,
            Money money) {
        this.id = null;
        this.ownerAccountId = ownerAccountId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.timestamp = timestamp;
        this.money = money;
    }

    public static class ActivityId {
        private final Long value;

        public ActivityId(Long value) {
            this.value = value;
        }

        public Long getValue() {
            return value;
        }
    }

    public ActivityId getId() {
        return id;
    }

    public void setId(ActivityId id) {
        this.id = id;
    }

    public Account.AccountId getOwnerAccountId() {
        return ownerAccountId;
    }

    public Account.AccountId getSourceAccountId() {
        return sourceAccountId;
    }

    public Account.AccountId getTargetAccountId() {
        return targetAccountId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Money getMoney() {
        return money;
    }
}