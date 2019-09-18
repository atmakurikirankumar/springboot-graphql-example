package com.kiran.explore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import graphql.ExecutionResult;

@RestController
@RequestMapping("/api")
public class PersonController {

	private GraphqlDemoService service;

	@Autowired
	public PersonController(GraphqlDemoService theSservice) {
		this.service=theSservice;
	}

	@PostMapping("/persons")
	public ResponseEntity<Object> personsGraph(@RequestBody String query) {
		ExecutionResult result = service.getGraphQL().execute(query);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

	@PostMapping("/addPerson")
	public ResponseEntity<Object> addPersonGraphql(@RequestBody String mutation) {
		ExecutionResult result = service.getGraphQL().execute(mutation);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
}
