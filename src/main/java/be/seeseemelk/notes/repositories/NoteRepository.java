package be.seeseemelk.notes.repositories;

import be.seeseemelk.notes.model.Note;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class NoteRepository implements PanacheRepository<Note>
{
	public Uni<List<Note>> findAllSortedByOrder()
	{
		return findAll().list();
	}
}
