package garage;

import java.util.Date;
import java.util.HashMap;

import garage.users.User;


public class ItemBoundary {

	private ItemId itemId;
	private String type;
	private String name;
	private boolean active;
	private Date createdTimestamp;
	private User createdBy;
	private Location location;
	private HashMap<String, Object> itemAttributes;

	public ItemBoundary() {
		super();
		
		// STUB IMPLEMENTATION
		this.setItemId(new ItemId());
		this.setType("Example type");
		this.setName("Example name");
		this.setActive(false);
		this.setCreatedTimestamp(new Date());
		this.setCreatedBy(new User());
		this.setLocation(new Location());
		this.itemAttributes = new HashMap<>();
		this.itemAttributes.put("ExampleAttribute", "The value");
	}

	public ItemBoundary(
			ItemId itemId,
			String type,
			String name,
			boolean active,
			Date createdTimestamp,
			User createdBy,
			Location location) {
		
		super();
		this.itemId = itemId;
		this.type = type;
		this.name = name;
		this.active = active;
		this.createdTimestamp = createdTimestamp;
		this.createdBy = createdBy;
		this.location = location;
		this.itemAttributes = new HashMap<>();
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

	public HashMap<String, Object> getItemAttributes() {
		return itemAttributes;
	}

	public void setItemAttributes(HashMap<String, Object> itemAttributes) {
		this.itemAttributes = itemAttributes;
	}

}