package be.seeseemelk.notes.test.e2e;

import be.seeseemelk.notes.model.Note;
import be.seeseemelk.notes.repositories.NoteRepository;
import be.seeseemelk.notes.rest.NoteController;
import be.seeseemelk.notes.test.AbstractTest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;

@TestHTTPEndpoint(NoteController.class)
public abstract class AbstractE2eTest extends AbstractTest
{
	@Inject
	NoteRepository repository;

	@BeforeEach
	void setUp()
	{
		await(repository.deleteAll());
	}

	@SuppressWarnings("SameParameterValue")
	protected Note createNote(Note note)
	{
		RestAssured.defaultParser = Parser.JSON;
		return given()
			.when()
				.body(note)
				.contentType(ContentType.APPLICATION_JSON.toString())
				.post("/")
			.then()
				.statusCode(200)
				.log()
				.body().extract().as(Note.class);
	}

	protected Note createNote()
	{
		return createNote(AbstractTest.BASE_NOTE_1);
	}
}
