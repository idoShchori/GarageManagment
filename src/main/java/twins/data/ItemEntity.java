package twins.data;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//USERS
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
//ID          | SPACE      |TYPE         | NAME          | ACTIVE       | CREATED_TIMESTAMP |USER_ID      | USER_SPACE  | LOCATION_LAT |LOCATION_LNG |ITEMS_ATTRIBUTES|
//VARCHAR(255)|VARCHAR(255)|VARCHAR(255) |VARCHAR(255)   | boolean      | TIMESTAMP         |VARCHAR(255) |VARCHAR(255) |  DOUBLE      |  DOUBLE     | CLOB           |
//<PK>        |<PK>        |
@Entity
@Table(name="ITEMS")
public class ItemEntity {
	
	@EmbeddedId
	private ItemIdPK itemIdPK;
	
	private String type;
	private String name;
	private Boolean active;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTimestamp;
	
	private String userEmail;
	private String userSpace;
	
	private double locationLat;
	private double locationLng;

	@Lob
	private String itemAttributes;

	public ItemEntity(){	
	}


	public ItemIdPK getItemIdPK() {
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
	
	
}
