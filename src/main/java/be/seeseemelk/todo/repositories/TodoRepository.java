package be.seeseemelk.todo.repositories;

import be.seeseemelk.todo.model.TodoItem;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TodoRepository implements PanacheRepository<TodoItem>
{
	public Uni<List<TodoItem>> findAllSortedByOrder()
	{
		return findAll().list();
	}
}
