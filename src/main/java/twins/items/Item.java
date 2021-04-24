package twins.items;

public class Item {
	private ItemIdBoundary itemId;
	
	public Item() {
		this.itemId = new ItemIdBoundary();
	}
	
	public Item(ItemIdBoundary id) {
		this.setItemId(id);
	}

	public ItemIdBoundary getItemId() {
		return itemId;
	}

	public void setItemId(ItemIdBoundary itemId) {
		this.itemId = itemId;
	}
	
	
}
