package se.artcomputer.edu.hexagonal.application.port.in;

import org.springframework.stereotype.Service;

@Service
public interface SendMoneyUseCase {

	boolean sendMoney(SendMoneyCommand command);

}