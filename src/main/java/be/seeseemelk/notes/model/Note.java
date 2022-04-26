package be.seeseemelk.notes.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@Entity
@NoArgsConstructor
public class Note
{
	@Id
	@GeneratedValue
	private long id;

	private String title;
	private String content;
	private LocalDateTime creationDate;
}
