package be.seeseemelk.todo.dto;

import be.seeseemelk.todo.model.TodoItem;
import lombok.Builder;
import lombok.Data;

import javax.annotation.processing.Generated;
import java.util.List;

@Data
@Builder
public class ListItemsResponse
{
	private List<TodoItem> items;

	@Generated("com.generator.Generator")
	public static ListItemsResponse ofItems(List<TodoItem> items)
	{
		return builder()
			.items(items)
			.build();
	}
}
