package be.seeseemelk.todo.test.e2e;

import be.seeseemelk.todo.model.TodoItem;
import be.seeseemelk.todo.repositories.TodoRepository;
import be.seeseemelk.todo.rest.TodoController;
import be.seeseemelk.todo.test.AbstractTest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;

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

	protected TodoItem createItem(TodoItem item)
	{
		RestAssured.defaultParser = Parser.JSON;
		return given()
			.when()
				.body(item)
				.contentType(ContentType.APPLICATION_JSON.toString())
				.post("/")
			.then()
				.statusCode(200)
				.log()
				.body().extract().as(TodoItem.class);
	}

	protected TodoItem createItem()
	{
		return createItem(AbstractTest.BASE_ITEM_1);
	}
}
