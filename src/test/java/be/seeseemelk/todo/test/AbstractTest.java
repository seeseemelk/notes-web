package be.seeseemelk.todo.test;

import be.seeseemelk.todo.model.TodoItem;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;

import java.time.Duration;
import java.time.LocalDateTime;


public abstract class AbstractTest
{
	public static final TodoItem BASE_ITEM_1 = TodoItem.builder()
		.title("Title 1")
		.content("Content 1")
		.creationDate(LocalDateTime.now())
		.build();

	public static final TodoItem BASE_ITEM_2 = TodoItem.builder()
		.title("Title 2")
		.content("Content 2")
		.creationDate(LocalDateTime.now())
		.build();

	public static final TodoItem EXAMPLE_ITEM_1 = TodoItem.builder()
		.id(1L)
		.title(BASE_ITEM_1.getTitle())
		.content(BASE_ITEM_1.getContent())
		.creationDate(BASE_ITEM_1.getCreationDate())
		.build();

	public static final TodoItem EXAMPLE_ITEM_2 = TodoItem.builder()
		.id(2L)
		.title(BASE_ITEM_2.getTitle())
		.content(BASE_ITEM_2.getContent())
		.creationDate(BASE_ITEM_2.getCreationDate())
		.build();

	public static <T> T await(Uni<T> uni)
	{
		return uni.await().atMost(Duration.ofSeconds(10L));
	}
}
