package se.artcomputer.edu.hexagonal.adapter.in;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import se.artcomputer.edu.hexagonal.application.port.in.SendMoneyCommand;
import se.artcomputer.edu.hexagonal.application.port.in.SendMoneyUseCase;
import se.artcomputer.edu.hexagonal.domain.Account;
import se.artcomputer.edu.hexagonal.domain.Money;

@RestController
class SendMoneyController {

	private final SendMoneyUseCase sendMoneyUseCase;

	@Autowired
	SendMoneyController(SendMoneyUseCase sendMoneyUseCase) {
		this.sendMoneyUseCase = sendMoneyUseCase;
	}

	@PostMapping(path = "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
	void sendMoney(
			@PathVariable("sourceAccountId") Long sourceAccountId,
			@PathVariable("targetAccountId") Long targetAccountId,
			@PathVariable("amount") Long amount) {

		SendMoneyCommand command = new SendMoneyCommand(
				new Account.AccountId(sourceAccountId),
				new Account.AccountId(targetAccountId),
				Money.of(amount));

		sendMoneyUseCase.sendMoney(command);
	}

}