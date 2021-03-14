package garage;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemConrtoller {
	
	
	
	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary createNewItem (@RequestBody ItemBoundary input, @PathVariable("userSpace") String space,@PathVariable("userEmail")String email) {
	
		input.setCreatedBy(new User(new UserId(space, email))); //???????
		input.setItemId(new ItemId());
		
		return input;
	}
	

}
