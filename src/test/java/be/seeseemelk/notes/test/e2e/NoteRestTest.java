package be.seeseemelk.notes.test.e2e;

import be.seeseemelk.notes.model.Note;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class NoteRestTest extends AbstractE2eTest
{
	@Test
	void testNoteDoesNotExist()
	{
		given()
			.when()
				.get("/1")
			.then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	void testNoteCanBeDeleted()
	{
		Note note = createNote();

		given()
			.when()
				.get("/{id}", note.getId())
			.then()
				.statusCode(HttpStatus.SC_OK);

		given()
			.when()
				.delete("/{id}", note.getId())
			.then()
				.statusCode(HttpStatus.SC_NO_CONTENT);

		given()
			.when()
				.get("/{id}", note.getId())
			.then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}
}
