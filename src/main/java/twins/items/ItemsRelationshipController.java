package twins.items;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import twins.logic.UpdatedItemsService;



@RestController
public class ItemsRelationshipController {
	private UpdatedItemsService updateItemService;

	@Autowired
	public ItemsRelationshipController(UpdatedItemsService updateItemService) {
		super();
		this.updateItemService = updateItemService;
	}
	
	// add operation for creation relationships between items
		@RequestMapping(method = RequestMethod.PUT,
				path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/children",
				consumes = MediaType.APPLICATION_JSON_VALUE)
		public void addChildToParent (
				@RequestBody ItemBoundary item,
				@PathVariable("userSpace") String userSpace,
				@PathVariable("userEmail") String userEmail,
				@PathVariable("itemSpace") String itemSpace,
				@PathVariable("itemId") String itemId) {
			this.updateItemService
				.addChildToParent(userSpace,userEmail,itemSpace,itemId,item);
		}
		
		// operation for getting all items of item of specific user
		@RequestMapping(path ="/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/children",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public ItemBoundary[] getAllChildren (
				@PathVariable("userSpace") String userSpace,
				@PathVariable("userEmail") String userEmail,
				@PathVariable("itemSpace") String itemSpace,
				@PathVariable("itemId") String itemId) {
			return this.updateItemService
				.getAllChildren(userSpace,userEmail,itemSpace,itemId)
				.toArray(new ItemBoundary[0]);
		}
		
		// operation for getting parent of item of specific user
		@RequestMapping(path ="/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/parents",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public ItemBoundary[] getAllParents (
				@PathVariable("userSpace") String userSpace,
				@PathVariable("userEmail") String userEmail,
				@PathVariable("itemSpace") String itemSpace,
				@PathVariable("itemId") String itemId) {
			Optional<List<ItemBoundary>> item = this.updateItemService.getAllParents(itemId,itemSpace);
			if(item.isPresent()) {
				return item.get().toArray(new ItemBoundary[0]);
			}else
				return new ItemBoundary[0];
		}
		


}
