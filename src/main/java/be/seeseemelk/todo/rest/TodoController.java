package be.seeseemelk.todo.rest;

import be.seeseemelk.todo.dto.CreateItemRequest;
import be.seeseemelk.todo.dto.ListItemsResponse;
import be.seeseemelk.todo.model.TodoItem;
import be.seeseemelk.todo.services.TodoService;
import io.smallrye.mutiny.Uni;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.reactive.ResponseStatus;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

@Path("/api/v1/todos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TodoController
{
	@Inject
	TodoService todos;

	@GET
	@Path("/")
	public Uni<ListItemsResponse> getAllItems()
	{
		return todos
			.getAllItems()
			.map(ListItemsResponse::ofItems);
	}

	@GET
	@Path("/{id}")
	public Uni<TodoItem> getItem(@PathParam("id") long id)
	{
		return todos.getItem(id);
	}
	
	@POST
	@Path("/")
	public Uni<TodoItem> createItem(CreateItemRequest request)
	{
		TodoItem item = TodoItem.builder()
			.creationDate(LocalDateTime.now())
			.content(request.getDescription())
			.title(request.getTitle())
			.build();
		return todos.createItem(item);
	}

	@DELETE
	@Path("/{id}")
	public Uni<Response> deleteItem(@PathParam("id") long id)
	{
		return todos.deleteItem(id)
			.replaceWith(() -> Response.noContent().build());
	}
}
