package twins.items;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import twins.logic.ItemsRelationshipService;



@RestController
public class ItemsRelationshipController {
	private ItemsRelationshipService updateItemService;

	@Autowired
	public ItemsRelationshipController(ItemsRelationshipService updateItemService) {
		super();
		this.updateItemService = updateItemService;
	}
	
	// add operation for creation relationships between items
	@RequestMapping(method = RequestMethod.PUT,
			path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/children",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addChildToParent (
			@RequestBody ItemIdBoundary item,
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId) {
		this.updateItemService
				.addChildToParent(userSpace, userEmail, itemSpace, itemId, item);
	}
	
	// operation for getting all items of item of specific user

	// invoke url, either with no optional parameters : /twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/children
	//             		  or with optional parameters : /twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/children?size=20&page=2
	@RequestMapping(path ="/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/children",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary[] getAllChildren (
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId,
			@RequestParam(name="size", required = false, defaultValue = "20") int size,
			@RequestParam(name="page", required = false, defaultValue = "0") int page) {
		return this.updateItemService
				.getAllChildren(userSpace, userEmail, itemSpace, itemId, size, page)
			.toArray(new ItemBoundary[0]);
	}
	
	// operation for getting parent of item of specific user
	// invoke url, either with no optional parameters : /twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/parents
	//             		  or with optional parameters : /twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/parents?size=20&page=2
	@RequestMapping(path ="/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/parents",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary[] getAllParents (
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId,
			@RequestParam(name="size", required = false, defaultValue = "20") int size,
			@RequestParam(name="page", required = false, defaultValue = "0") int page) {
		List<ItemBoundary> item = this.updateItemService.getAllParents(userSpace, userEmail, itemSpace, itemId, size, page);
		return item.toArray(new ItemBoundary[0]);
	}
}
