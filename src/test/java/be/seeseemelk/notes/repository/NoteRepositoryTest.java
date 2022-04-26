package be.seeseemelk.notes.repository;

import be.seeseemelk.notes.model.Note;
import be.seeseemelk.notes.repositories.NoteRepository;
import be.seeseemelk.notes.test.AbstractTest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

@QuarkusTest
public class NoteRepositoryTest extends AbstractTest
{
	@Inject
	NoteRepository repository;

	@Test
	void testFindAllSortedByOrder()
	{
		Note savedNote1 = await(repository.persistAndFlush(BASE_NOTE_1));
		Note savedNote2 = await(repository.persistAndFlush(BASE_NOTE_2));

		List<Note> notes = await(repository.findAllSortedByOrder());
		assertThat(notes, contains(savedNote1, savedNote2));
	}
}
