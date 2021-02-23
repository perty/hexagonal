package se.artcomputer.edu.hexagonal.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

interface AccountRepository extends JpaRepository<AccountJpaEntity, Long> {


}