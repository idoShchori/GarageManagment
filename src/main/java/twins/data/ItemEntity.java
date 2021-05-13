package twins.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//USERS
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//ID          | SPACE      |TYPE         | NAME          | ACTIVE       | CREATED_TIMESTAMP |USER_ID      | USER_SPACE  | LOCATION_LAT |LOCATION_LNG |ITEMS_ATTRIBUTES|PARENT_ID   | PARENT_SPACE|
//VARCHAR(255)|VARCHAR(255)|VARCHAR(255) |VARCHAR(255)   | boolean      | TIMESTAMP         |VARCHAR(255) |VARCHAR(255) |  DOUBLE      |  DOUBLE     | CLOB           |VARCHAR(255)|VARCHAR(255)  
//<PK>        |<PK>        |
@Entity
@Table(name = "ITEMS")
public class ItemEntity {

	private ItemIdPK itemIdPK;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private String userEmail;
	private String userSpace;
	private double locationLat;
	private double locationLng;
	private String itemAttributes;
	private Set<ItemEntity> children;
	private ItemEntity parent;

	public ItemEntity() {
		children = new HashSet<>();
	}

	@EmbeddedId
	public ItemIdPK getItemId() {
		return itemIdPK;
	}

	public void setItemId(ItemIdPK itemIdPK) {
		this.itemIdPK = itemIdPK;
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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserSpace() {
		return userSpace;
	}

	public void setUserSpace(String userSpace) {
		this.userSpace = userSpace;
	}

	public double getLocationLat() {
		return locationLat;
	}

	public void setLocationLat(double locationLat) {
		this.locationLat = locationLat;
	}

	public double getLocationLng() {
		return locationLng;
	}

	public void setLocationLng(double locationLng) {
		this.locationLng = locationLng;
	}

	@Lob
	public String getItemAttributes() {
		return itemAttributes;
	}

	public void setItemAttributes(String itemAttributes) {
		this.itemAttributes = itemAttributes;
	}

	@OneToMany(mappedBy = "parent" , fetch = FetchType.LAZY)
	public Set<ItemEntity> getChildren() {
		return children;
	}

	public void setChildren(Set<ItemEntity> children) {
		this.children = children;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public ItemEntity getParent() {
		return parent;
	}

	public void setParent(ItemEntity parent) {
		this.parent = parent;
	}

	public void addItem(ItemEntity child) {
		children.add(child);
		child.setParent(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemIdPK == null) ? 0 : itemIdPK.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemEntity other = (ItemEntity) obj;
		if (itemIdPK == null) {
			if (other.itemIdPK != null)
				return false;
		} else if (!itemIdPK.equals(other.itemIdPK))
			return false;
		return true;
	}

}
