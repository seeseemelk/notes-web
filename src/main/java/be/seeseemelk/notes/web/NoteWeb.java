package be.seeseemelk.notes.web;

import be.seeseemelk.notes.model.Note;
import be.seeseemelk.notes.services.NoteService;
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
public class NoteWeb
{
	@Inject
	NoteService notes;

	@CheckedTemplate
	public static class Template
	{
		public static native TemplateInstance view(Uni<List<Note>> notes);
		public static native TemplateInstance edit(Uni<Note> note);
	}

	@GET
	@Path("/")
	public TemplateInstance index()
	{
		return Template.view(notes.getAllNotes());
	}

	@GET
	@Path("/view/{id}")
	public TemplateInstance view(@PathParam("id") long id)
	{
		return Template.view(
			notes
			.getNote(id)
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
		return notes.deleteNote(id)
			.map(unused -> Response.seeOther(URI.create("/view")).build());
	}

	@POST
	@Path("/create")
	public Uni<Response> create(
		@FormParam("title") String title,
		@FormParam("content") String content
	)
	{
		Note newItem = Note.builder()
			.creationDate(LocalDateTime.now())
			.title(title)
			.content(content)
			.build();
		Uni<Note> savedItem = notes.createNote(newItem);
		return savedItem
			.map(Note::getId)
			.map(id -> Response
				.seeOther(URI.create("/view/" + id))
				.build());
	}

	@GET
	@Path("/edit/{id}")
	public TemplateInstance edit(@PathParam("id") long id)
	{
		return Template.edit(notes.getNote(id));
	}

	@POST
	@Path("/edit/{id}")
	public Uni<Response> edit(
		@PathParam("id") long id,
		@FormParam("title") String title,
		@FormParam("content") String content
	)
	{
		return notes.getNote(id)
			.map(item -> {
				item.setTitle(title);
				item.setContent(content);
				return item;
			})
			.flatMap(notes::updateNote)
			.map(item -> Response.seeOther(URI.create("/view/" + item.getId())).build());
	}
}
