package be.seeseemelk.notes.services;

import be.seeseemelk.notes.model.Note;
import be.seeseemelk.notes.repositories.NoteRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class NoteService
{
	@Inject
	NoteRepository repository;

	public Uni<List<Note>> getAllNotes()
	{
		return repository.findAllSortedByOrder();
	}

	public Uni<Note> getNote(long id)
	{
		return repository
			.findById(id)
			.onItem().ifNull().failWith(NotFoundException::new);
	}

	public Uni<Note> createNote(Note note)
	{
		return repository.persistAndFlush(note);
	}

	public Uni<Note> updateNote(Note note)
	{
		return repository.persistAndFlush(note);
	}

	public Uni<Void> deleteNote(long id)
	{
		return repository
			.deleteById(id)
			.map(bool -> bool ? bool : null)
			.onItem().ifNull().failWith(NotFoundException::new)
			.onItem().ifNotNull().transformToUni(bool -> repository.flush())
			.replaceWithVoid();
	}
}
