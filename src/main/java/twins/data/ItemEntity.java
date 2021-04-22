package twins.data;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import twins.items.ItemId;
import twins.items.Location;
import twins.users.User;

//USERS
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
//ITEM_ID       |TYPE         | NAME          | ACTIVE       | CREATED_TIMESTAMP |CREATED_BY |LOCATION |ITEMS_ATTRIBUTES|
//              |VARCHAR(255) |VARCHAR(255)   | boolean      | TIMESTAMP         |           |         | CLOB           |

@Entity
@Table(name="ITEMS")
public class ItemEntity {
	
	private ItemId itemId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private User createdBy;
	private Location location;
	private String itemAttributes;

	public ItemEntity(){	
	}

	@Transient
	public ItemId getItemId() {
		return itemId;
	}

	@Transient
	public void setItemId(ItemId itemId) {
		this.itemId = itemId;
	}

	@Id
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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	@Transient
	public User getCreatedBy() {
		return createdBy;
	}

	@Transient
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	@Transient
	public Location getLocation() {
		return location;
	}
	@Transient
	public void setLocation(Location location) {
		this.location = location;
	}

	//TODO map to database as CLOB
	@Lob
	public String getItemAttributes() {
		return itemAttributes;
	}


	public void setItemAttributes(String itemAttributes) {
		this.itemAttributes = itemAttributes;
	}
	
	
}
