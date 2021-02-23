package se.artcomputer.edu.hexagonal.application;

import se.artcomputer.edu.hexagonal.domain.Money;

public class MoneyTransferProperties {

  public static final Money maximumTransferThreshold = Money.of(1_000_000L);

  public Money getMaximumTransferThreshold() {
    return maximumTransferThreshold;
  }
}