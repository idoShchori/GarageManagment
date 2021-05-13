package twins.data;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//	OPERATIONS
//	--------------------------------------------------------------------------------------------------------------------------------------------------------------
//	OPERATION_SPACE |OPERATION_ID | ITEM_SPACE 	| ITEM_ID	   | USER_SPACE   | USER_EMAIL	| TYPE			|CREATED_TIME_STAMP	|ATTRIBUTES
//	VARCHAR(255)  	|VARCHAR(255) | VARCHAR(255)| VARCHAR(255) | VARCHAR(255) | VARCHAR(255)| VARCHAR(255)	|TIMESTAMP			|CLOB	
//	<PK>			|<PK>		  |

@Entity
@Table(name = "OPERATIONS")
public class OperationEntity {

	private OperationIdPK operationIdPK;
	private String itemSpace;
	private String itemId;
	private String userSpace;
	private String userEmail;
	private String type;
	private Date createdTimestamp;
	private String operationAttributes;

	public OperationEntity() {
	}

	@EmbeddedId
	public OperationIdPK getOperationIdPK() {
		return operationIdPK;
	}

	public void setOperationIdPK(OperationIdPK operationIdPK) {
		this.operationIdPK = operationIdPK;
	}

	public String getItemSpace() {
		return itemSpace;
	}

	public void setItemSpace(String itemSpace) {
		this.itemSpace = itemSpace;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getUserSpace() {
		return userSpace;
	}

	public void setUserSpace(String userSpace) {
		this.userSpace = userSpace;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	
	@Lob
	public String getOperationAttributes() {
		return operationAttributes;
	}

	public void setOperationAttributes(String operationAttributes) {
		this.operationAttributes = operationAttributes;
	}

}
