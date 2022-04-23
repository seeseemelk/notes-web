package be.seeseemelk.todo.model;

import lombok.*;
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
public class TodoItem
{
	@Id
	@GeneratedValue
	private long id;

	private String title;
	private String content;
	private LocalDateTime creationDate;
}
