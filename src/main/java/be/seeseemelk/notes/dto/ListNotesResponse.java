package be.seeseemelk.notes.dto;

import be.seeseemelk.notes.model.Note;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListNotesResponse
{
	private List<Note> notes;

	public static ListNotesResponse ofNotes(List<Note> notes)
	{
		return builder()
			.notes(notes)
			.build();
	}
}
