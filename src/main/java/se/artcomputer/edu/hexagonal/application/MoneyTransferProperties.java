package se.artcomputer.edu.hexagonal.application;

import org.springframework.stereotype.Component;
import se.artcomputer.edu.hexagonal.domain.Money;

@Component
public class MoneyTransferProperties {

    public static final Money maximumTransferThreshold = Money.of(1_000_000L);

    public Money getMaximumTransferThreshold() {
        return maximumTransferThreshold;
    }
}