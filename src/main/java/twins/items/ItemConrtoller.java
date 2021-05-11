package twins.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import twins.logic.ItemsService;



@RestController
public class ItemConrtoller {
	private ItemsService itemService;
	
	@Autowired
	public ItemConrtoller(ItemsService itemService) {
		this.itemService=itemService;
	}
	
	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary createNewItem(
			@RequestBody ItemBoundary input,
			@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {

		return itemService.createItem(space, email, input);
	}

	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateItem(
			@RequestBody ItemBoundary update,
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String email,
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId) {
		itemService.updateItem(userSpace, email, itemSpace, itemId,update);
	}

	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary getItem(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String email,
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId) {

		return  itemService.getSpecificItem(userSpace, email, itemSpace, itemId);

	}

	// invoke url, either with no optional parameters : /twins/items/{userSpace}/{userEmail}
	//             		  or with optional parameters : /twins/items/{userSpace}/{userEmail}?size=20&page=2
	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary[] getAllItems(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String email,
			@RequestParam(name="size", required = false, defaultValue = "20") int size,
			@RequestParam(name="page", required = false, defaultValue = "0") int page) {

		return itemService.getAllItems(userSpace, email, size, page)
							.toArray(new ItemBoundary[0]);

	}
	
}
