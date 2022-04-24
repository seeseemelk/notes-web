package be.seeseemelk.todo.rest;

import be.seeseemelk.todo.model.TodoItem;
import be.seeseemelk.todo.services.TodoService;
import be.seeseemelk.todo.test.AbstractTest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Uni;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@TestHTTPEndpoint(TodoController.class)
public class TodoControllerTest extends AbstractTest
{
	@InjectMock
	TodoService todos;

	@Test
	public void testGetAllWithNoTodoItems()
	{
		Mockito
			.when(todos.getAllItems())
			.thenReturn(Uni.createFrom().item(Collections.emptyList()));

		given()
			.when()
				.get("/")
			.then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body("items", hasSize(0));
	}

	@Test
	public void testGetAllWithOneTodoItem()
	{
		Mockito
			.when(todos.getAllItems())
			.thenReturn(Uni.createFrom().item(List.of(EXAMPLE_ITEM_1)));

		given()
			.when()
				.get("/")
			.then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body("items", hasSize(1))
				.body("items[0].id", equalTo((int) EXAMPLE_ITEM_1.getId()))
				.body("items[0].title", equalTo(EXAMPLE_ITEM_1.getTitle()))
				.body("items[0].content", equalTo(EXAMPLE_ITEM_1.getContent()))
				.body("items[0].creationDate", equalTo(toString(EXAMPLE_ITEM_1.getCreationDate())));
	}

	@Test
	public void testGetAllWithTwoTodoItems()
	{
		Mockito
			.when(todos.getAllItems())
			.thenReturn(Uni.createFrom().item(List.of(EXAMPLE_ITEM_1, EXAMPLE_ITEM_2)));

		given()
			.when()
				.get("/")
			.then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body("items", hasSize(2))
				.body("items[0].title", equalTo(EXAMPLE_ITEM_1.getTitle()))
				.body("items[1].title", equalTo(EXAMPLE_ITEM_2.getTitle()));
	}

	@Test
	public void testGetItemThatExists()
	{
		Mockito
			.when(todos.getItem(2L))
			.thenReturn(Uni.createFrom().item(EXAMPLE_ITEM_2));

		given()
			.when()
				.get("/2")
			.then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body("id", equalTo((int) EXAMPLE_ITEM_2.getId()))
				.body("title", equalTo(EXAMPLE_ITEM_2.getTitle()))
				.body("content", equalTo(EXAMPLE_ITEM_2.getContent()))
				.body("creationDate", equalTo(toString(EXAMPLE_ITEM_2.getCreationDate())));
	}

	@Test
	public void testGetItemThatDoesNotExist()
	{
		Mockito
			.when(todos.getItem(2L))
			.thenReturn(Uni.createFrom().failure(NotFoundException::new));

		given()
			.when()
				.get("/2")
			.then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}
}
