package be.seeseemelk.todo.services;

import be.seeseemelk.todo.model.TodoItem;
import be.seeseemelk.todo.repositories.TodoRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class TodoService
{
	@Inject
	TodoRepository repository;

	public Uni<List<TodoItem>> getAllItems()
	{
		return repository
			.findAllSortedByOrder();
	}

	public Uni<TodoItem> getItem(long id)
	{
		return repository
			.findById(id)
			.onItem().ifNull().failWith(NotFoundException::new);
	}

	public Uni<TodoItem> createItem(TodoItem item)
	{
		return repository
			.persistAndFlush(item);
	}

	public Uni<Boolean> deleteItem(long id)
	{
		return repository
			.deleteById(id);
	}
}
