package twins.logic;


import java.util.List;

import twins.items.ItemBoundary;
import twins.items.ItemIdBoundary;

public interface UpdatedItemsService extends ItemsService{

	public void addChildToParent(String userSpace,String userEmail,String itemSpace, String itemId,ItemIdBoundary item);
	public List<ItemBoundary> getAllChildren(String userSpace,String userEmail,String itemSpace, String itemId);
	public List<ItemBoundary> getAllParents(String childSpace, String childId);
	
}
