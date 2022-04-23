package be.seeseemelk.todo.test.e2e;

import be.seeseemelk.todo.repositories.TodoRepository;
import be.seeseemelk.todo.rest.TodoController;
import be.seeseemelk.todo.test.AbstractTest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;

@TestHTTPEndpoint(TodoController.class)
public abstract class AbstractE2eTest extends AbstractTest
{
	@Inject
	TodoRepository repository;

	@BeforeEach
	void setUp()
	{
		await(repository.deleteAll());
	}
}
