package twins.data;

import java.util.Date;
import java.util.HashMap;

import twins.items.ItemId;
import twins.items.Location;
import twins.users.User;

public class ItemEntity {
	
	private ItemId itemId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private User createdBy;
	private Location location;
	private HashMap<String, Object> itemAttributes;

	public ItemEntity(){
		
	}

	public ItemId getItemId() {
		return itemId;
	}

	public void setItemId(ItemId itemId) {
		this.itemId = itemId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public HashMap<String, Object> getItemAttributes() {
		return itemAttributes;
	}

	public void setItemAttributes(HashMap<String, Object> itemAttributes) {
		this.itemAttributes = itemAttributes;
	}
	
	
}
