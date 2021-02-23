package se.artcomputer.edu.hexagonal.adapter.out;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
class AccountJpaEntity {

	@Id
	@GeneratedValue
	private Long id;

	public Long getId() {
		return id;
	}
}