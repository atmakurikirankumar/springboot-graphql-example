package com.kiran.explore;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Service
public class GraphqlDemoService {

	@Autowired
	private PersonRepo repo;

	private GraphQL graphql;

	@Value(value = "classpath:person.graphqls")
	private Resource resource;

	@PostConstruct
	public void loadSchema() throws IOException {
		File schema = resource.getFile();
		TypeDefinitionRegistry registry = new SchemaParser().parse(schema);
		RuntimeWiring wiring = buildWiring();
		GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
		graphql = GraphQL.newGraphQL(graphQLSchema).build();
	}

	private RuntimeWiring buildWiring() {
		DataFetcher<List<Person>> fetcher1 = data -> (List<Person>) repo.findAll();

		DataFetcher<Person> fetcher2 = data -> repo.findByEmail(data.getArgument("email"));

		DataFetcher<Person> fetcher3 = data -> buildPerson(data);

		return RuntimeWiring.newRuntimeWiring()
				.type("Query",typewriting -> typewriting.dataFetcher("getAllPersons", fetcher1).dataFetcher("findSinglePerson", fetcher2))
				.type("Mutation", typewriting -> typewriting.dataFetcher("addNewPerson", fetcher3)).build();
	}

	private Person buildPerson(DataFetchingEnvironment data) {
		String id = data.getArgument("id");
		int idInt = Integer.parseInt(id);

		String name = data.getArgument("name");
		String mobile = data.getArgument("mobile");
		String email = data.getArgument("email");

		Person person = new Person(idInt, name, mobile, email);

		return repo.save(person);
	}

	public GraphQL getGraphQL() {
		return graphql;
	}
}
