package be.seeseemelk.todo.web;

import be.seeseemelk.todo.dto.CreateItemRequest;
import be.seeseemelk.todo.model.TodoItem;
import be.seeseemelk.todo.services.TodoService;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Path("")
public class TodoWeb
{
	@Inject
	TodoService todos;

	@CheckedTemplate
	public static class Template
	{
		public static native TemplateInstance view(Uni<List<TodoItem>> items);
		public static native TemplateInstance edit(Uni<TodoItem> item);
	}

	@GET
	@Path("/")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance index()
	{
		return Template.view(todos.getAllItems());
	}

	@GET
	@Path("/view/{id}")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance view(@PathParam("id") long id)
	{
		return Template.view(
			todos
			.getItem(id)
			.map(Collections::singletonList)
		);
	}

	@GET
	@Path("/create")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance create()
	{
		return Template.edit(Uni.createFrom().nullItem());
	}

	@GET
	@Path("/delete/{id}")
	@Produces(MediaType.TEXT_HTML)
	public Uni<TemplateInstance> delete(@PathParam("id") long id)
	{
		return todos.deleteItem(id)
			.onItem().transform(unused -> index());
	}

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public Uni<Response> create(
		@FormParam("title") String title,
		@FormParam("content") String content
	)
	{
		TodoItem newItem = TodoItem.builder()
			.creationDate(LocalDateTime.now())
			.title(title)
			.content(content)
			.build();
		Uni<TodoItem> savedItem = todos.createItem(newItem);
		return savedItem
			.map(TodoItem::getId)
			.map(id -> Response
				.created(URI.create("/view/" + id))
				.entity(view(id))
				.build());
	}

	@GET
	@Path("/edit/{id}")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance edit(@PathParam("id") long id)
	{
		return Template.edit(todos.getItem(id));
	}

	@PUT
	@Path("/edit/{id}")
	public void edit(@PathParam("id") long id, CreateItemRequest request)
	{

	}
}
