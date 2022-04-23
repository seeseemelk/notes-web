package be.seeseemelk.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.FormParam;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequest
{
	@FormParam("title")
	private String title;

	@FormParam("content")
	private String description;
}
