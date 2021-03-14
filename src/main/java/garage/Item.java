package garage;

public class Item {
	private ItemId itemId;
	
	public Item() {
		this.itemId = new ItemId();
	}

	public ItemId getItemId() {
		return itemId;
	}

	public void setItemId(ItemId itemId) {
		this.itemId = itemId;
	}
	
	
}
