package be.seeseemelk.notes.rest;

import be.seeseemelk.notes.dto.CreateNoteRequest;
import be.seeseemelk.notes.dto.ListNotesResponse;
import be.seeseemelk.notes.model.Note;
import be.seeseemelk.notes.services.NoteService;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

@Path("/api/v1/notes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NoteController
{
	@Inject
	NoteService notes;

	@GET
	@Path("/")
	public Uni<ListNotesResponse> getAllNotes()
	{
		return notes
			.getAllNotes()
			.map(ListNotesResponse::ofNotes);
	}

	@GET
	@Path("/{id}")
	public Uni<Note> getNote(@PathParam("id") long id)
	{
		return notes.getNote(id);
	}
	
	@POST
	@Path("/")
	public Uni<Note> createNote(CreateNoteRequest request)
	{
		Note note = Note.builder()
			.creationDate(LocalDateTime.now())
			.content(request.getContent())
			.title(request.getTitle())
			.build();
		return notes.createNote(note);
	}

	@DELETE
	@Path("/{id}")
	public Uni<Response> deleteNote(@PathParam("id") long id)
	{
		return notes.deleteNote(id)
			.replaceWith(() -> Response.noContent().build());
	}
}
