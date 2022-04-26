package be.seeseemelk.todo.web;

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
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_HTML)
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
	public TemplateInstance index()
	{
		return Template.view(todos.getAllItems());
	}

	@GET
	@Path("/view/{id}")
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
	public TemplateInstance create()
	{
		return Template.edit(Uni.createFrom().nullItem());
	}

	@GET
	@Path("/delete/{id}")
	public Uni<Response> delete(@PathParam("id") long id)
	{
		return todos.deleteItem(id)
			.map(unused -> Response.seeOther(URI.create("/view")).build());
	}

	@POST
	@Path("/create")
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
				.seeOther(URI.create("/view/" + id))
				.build());
	}

	@GET
	@Path("/edit/{id}")
	public TemplateInstance edit(@PathParam("id") long id)
	{
		return Template.edit(todos.getItem(id));
	}

	@POST
	@Path("/edit/{id}")
	public Uni<Response> edit(
		@PathParam("id") long id,
		@FormParam("title") String title,
		@FormParam("content") String content
	)
	{
		return todos.getItem(id)
			.map(item -> {
				item.setTitle(title);
				item.setContent(content);
				return item;
			})
			.flatMap(todos::updateItem)
			.map(item -> Response.seeOther(URI.create("/view/" + item.getId())).build());
	}
}
