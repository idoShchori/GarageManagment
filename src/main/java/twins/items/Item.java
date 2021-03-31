package twins.items;

public class Item {
	private ItemId itemId;
	
	public Item() {
		this.itemId = new ItemId();
	}
	
	public Item(ItemId id) {
		this.setItemId(id);
	}

	public ItemId getItemId() {
		return itemId;
	}

	public void setItemId(ItemId itemId) {
		this.itemId = itemId;
	}
	
	
}
