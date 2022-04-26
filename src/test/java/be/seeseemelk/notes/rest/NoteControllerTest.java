package be.seeseemelk.notes.rest;

import be.seeseemelk.notes.services.NoteService;
import be.seeseemelk.notes.test.AbstractTest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Uni;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@TestHTTPEndpoint(NoteController.class)
public class NoteControllerTest extends AbstractTest
{
	@InjectMock
	NoteService notes;

	@Test
	public void testGetAllWithNoTodoItems()
	{
		Mockito
			.when(notes.getAllNotes())
			.thenReturn(Uni.createFrom().item(Collections.emptyList()));

		given()
			.when()
				.get("/")
			.then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body("notes", hasSize(0));
	}

	@Test
	public void testGetAllWithOneTodoItem()
	{
		Mockito
			.when(notes.getAllNotes())
			.thenReturn(Uni.createFrom().item(List.of(EXAMPLE_NOTE_1)));

		given()
			.when()
				.get("/")
			.then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body("notes", hasSize(1))
				.body("notes[0].id", equalTo((int) EXAMPLE_NOTE_1.getId()))
				.body("notes[0].title", equalTo(EXAMPLE_NOTE_1.getTitle()))
				.body("notes[0].content", equalTo(EXAMPLE_NOTE_1.getContent()))
				.body("notes[0].creationDate", equalTo(toString(EXAMPLE_NOTE_1.getCreationDate())));
	}

	@Test
	public void testGetAllWithTwoNotes()
	{
		Mockito
			.when(notes.getAllNotes())
			.thenReturn(Uni.createFrom().item(List.of(EXAMPLE_NOTE_1, EXAMPLE_NOTE_2)));

		given()
			.when()
				.get("/")
			.then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body("notes", hasSize(2))
				.body("notes[0].title", equalTo(EXAMPLE_NOTE_1.getTitle()))
				.body("notes[1].title", equalTo(EXAMPLE_NOTE_2.getTitle()));
	}

	@Test
	public void testGetItemThatNotes()
	{
		Mockito
			.when(notes.getNote(2L))
			.thenReturn(Uni.createFrom().item(EXAMPLE_NOTE_2));

		given()
			.when()
				.get("/2")
			.then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body("id", equalTo((int) EXAMPLE_NOTE_2.getId()))
				.body("title", equalTo(EXAMPLE_NOTE_2.getTitle()))
				.body("content", equalTo(EXAMPLE_NOTE_2.getContent()))
				.body("creationDate", equalTo(toString(EXAMPLE_NOTE_2.getCreationDate())));
	}

	@Test
	public void testGetNoteThatDoesNotExist()
	{
		Mockito
			.when(notes.getNote(2L))
			.thenReturn(Uni.createFrom().failure(NotFoundException::new));

		given()
			.when()
				.get("/2")
			.then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}
}
