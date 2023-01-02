package se.artcomputer.edu.hexagonal.adapter.out;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
class AccountJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }
}