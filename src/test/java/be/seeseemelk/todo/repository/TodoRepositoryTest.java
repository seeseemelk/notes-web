package be.seeseemelk.todo.repository;

import be.seeseemelk.todo.model.TodoItem;
import be.seeseemelk.todo.repositories.TodoRepository;
import be.seeseemelk.todo.test.AbstractTest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

@QuarkusTest
public class TodoRepositoryTest extends AbstractTest
{
	@Inject
	TodoRepository repository;

	@Test
	void testFindAllSortedByOrder()
	{
		TodoItem savedItem1 = await(repository.persistAndFlush(BASE_ITEM_1));
		TodoItem savedItem2 = await(repository.persistAndFlush(BASE_ITEM_2));

		List<TodoItem> items = await(repository.findAllSortedByOrder());
		assertThat(items, contains(savedItem1, savedItem2));
	}
}
