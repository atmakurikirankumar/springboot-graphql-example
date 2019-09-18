package com.kiran.explore;

import org.springframework.data.repository.CrudRepository;

public interface PersonRepo extends CrudRepository<Person, Integer> {

	Person findByEmail(String email);

}
