package garage;

import java.util.Date;
import java.util.HashMap;

public class ItemBoundary {
	

	private ItemId itemId;
	private String type;
	private String name;
	private boolean active;
	private Date createdTimestamp;
	private User createdBy;
	private Location location;
	
	private HashMap<String,Object> attribute;
	
	public ItemBoundary() {
		super();
	}


	public ItemBoundary(ItemId itemId, String type, String name, boolean active, Date createdTimestamp, User createdBy,
			Location location) {
		super();
		this.itemId = itemId;
		this.type = type;
		this.name = name;
		this.active = active;
		this.createdTimestamp = createdTimestamp;
		this.createdBy = createdBy;
		this.location = location;
		this.attribute = new HashMap<>();
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


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
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




	public HashMap<String, Object> getAttribute() {
		return attribute;
	}




	public void setAttribute(HashMap<String, Object> attribute) {
		this.attribute = attribute;
	}
	
	
	
	
	
	

	

}
