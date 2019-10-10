package com.poc.requestapproval.repository;

import com.poc.requestapproval.domain.Authority;
import com.poc.requestapproval.domain.UserAuthorityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {

	Optional<Authority> findOneByName(UserAuthorityType name);

}
