package se.artcomputer.edu.hexagonal.adapter.out;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity")
class ActivityJpaEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private LocalDateTime timestamp;

	@Column
	private Long ownerAccountId;

	@Column
	private Long sourceAccountId;

	@Column
	private Long targetAccountId;

	@Column
	private Long amount;

	public ActivityJpaEntity() {
	}

	public ActivityJpaEntity(Long id, LocalDateTime timestamp, Long ownerAccountId, Long sourceAccountId, Long targetAccountId, Long amount) {
		this.id = id;
		this.timestamp = timestamp;
		this.ownerAccountId = ownerAccountId;
		this.sourceAccountId = sourceAccountId;
		this.targetAccountId = targetAccountId;
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Long getOwnerAccountId() {
		return ownerAccountId;
	}

	public void setOwnerAccountId(Long ownerAccountId) {
		this.ownerAccountId = ownerAccountId;
	}

	public Long getSourceAccountId() {
		return sourceAccountId;
	}

	public void setSourceAccountId(Long sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}

	public Long getTargetAccountId() {
		return targetAccountId;
	}

	public void setTargetAccountId(Long targetAccountId) {
		this.targetAccountId = targetAccountId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}
}