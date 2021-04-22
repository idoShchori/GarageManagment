package twins.logic;


import java.util.List;
import java.util.Optional;

import twins.items.ItemBoundary;

public interface UpdatedItemsService extends ItemsService{

	public void addChildToParent(String userSpace,String userEmail,String itemSpace, String itemId,ItemBoundary item);
	public List<ItemBoundary> getAllChildren(String userSpace,String userEmail,String itemSpace, String itemId);
	public Optional<List<ItemBoundary>>getAllParents(String childSpace, String childId);
	
}
