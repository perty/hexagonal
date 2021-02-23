package se.artcomputer.edu.hexagonal.adapter.out;

import org.springframework.stereotype.Component;
import se.artcomputer.edu.hexagonal.application.port.out.LoadAccountPort;
import se.artcomputer.edu.hexagonal.application.port.out.UpdateAccountStatePort;
import se.artcomputer.edu.hexagonal.domain.Account;
import se.artcomputer.edu.hexagonal.domain.Activity;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Component
class AccountPersistenceAdapter implements
		LoadAccountPort,
		UpdateAccountStatePort {

	private final AccountRepository accountRepository;
	private final ActivityRepository activityRepository;
	private final AccountMapper accountMapper;

	AccountPersistenceAdapter(AccountRepository accountRepository, ActivityRepository activityRepository, AccountMapper accountMapper) {
		this.accountRepository = accountRepository;
		this.activityRepository = activityRepository;
		this.accountMapper = accountMapper;
	}

	@Override
	public Account loadAccount(
					Account.AccountId accountId,
					LocalDateTime baselineDate) {

		AccountJpaEntity account =
				accountRepository.findById(accountId.getValue())
						.orElseThrow(EntityNotFoundException::new);

		List<ActivityJpaEntity> activities =
				activityRepository.findByOwnerSince(
						accountId.getValue(),
						baselineDate);

		Long withdrawalBalance = orZero(activityRepository
				.getWithdrawalBalanceUntil(
						accountId.getValue(),
						baselineDate));

		Long depositBalance = orZero(activityRepository
				.getDepositBalanceUntil(
						accountId.getValue(),
						baselineDate));

		return accountMapper.mapToDomainEntity(
				account,
				activities,
				withdrawalBalance,
				depositBalance);

	}

	private Long orZero(Long value){
		return value == null ? 0L : value;
	}


	@Override
	public void updateActivities(Account account) {
		for (Activity activity : account.getActivityWindow().getActivities()) {
			if (activity.getId() == null) {
				activityRepository.save(accountMapper.mapToJpaEntity(activity));
			}
		}
	}

}