package be.seeseemelk.todo.test.e2e;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class TodoRestTest extends AbstractE2eTest
{
	@Test
	void testItemDoesNotExist()
	{
		given()
			.when()
				.get("/1")
			.then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}
}
