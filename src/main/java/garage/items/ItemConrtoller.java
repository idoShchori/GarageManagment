package garage.items;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import garage.users.User;
import garage.users.UserId;


@RestController
public class ItemConrtoller {

	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary createNewItem(@RequestBody ItemBoundary input, @PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {

		input.setCreatedBy(new User(new UserId(space, email)));
		input.setItemId(new ItemId());

		return input;
	}

	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateItem(@RequestBody ItemBoundary input, @PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String email, @PathVariable("itemSpace") String itemSpace,
			@PathVariable("userEmail") String itemId) {

		// STUB
		// Update the item into the database

	}

	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary getItem(@RequestBody ItemBoundary input, @PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String email, @PathVariable("itemSpace") String itemSpace,
			@PathVariable("userEmail") String itemId) {

		return new ItemBoundary();

	}

	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}/",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary[] getItem(@RequestBody ItemBoundary input, @PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String email) {

		return new ItemBoundary[] { new ItemBoundary() };

	}

}
