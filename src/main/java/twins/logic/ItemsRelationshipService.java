package twins.logic;


import java.util.List;

import twins.items.ItemBoundary;
import twins.items.ItemIdBoundary;

public interface ItemsRelationshipService extends ItemsService {

	public void addChildToParent(String userSpace,String userEmail,String itemSpace, String itemId,ItemIdBoundary item);
	@Deprecated
	public List<ItemBoundary> getAllChildren(String userSpace,String userEmail,String itemSpace, String itemId);
	public List<ItemBoundary> getAllChildren(String userSpace,String userEmail,String itemSpace, String itemId, int size, int page);
	@Deprecated
	public List<ItemBoundary> getAllParents(String childSpace, String childId);
	public List<ItemBoundary> getAllParents(String userSpace, String userEmail, String childSpace, String childId, int size, int page);
	
}
