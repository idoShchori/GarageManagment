package twins.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


//	OPERATIONS
//	--------------------------------------------------------------------------------------------------------------------------------------------------------------
//	OPERATION_SPACE |OPERATION_ID | ITEM_SPACE 	| ITEM_ID	   | USER_SPACE   | USER_EMAIL	| TYPE			|CREATED_TIME_STAMP	|	ATTRIBUTES
//	VARCHAR(255)  	|VARCHAR(255) | VARCHAR(255)| VARCHAR(255) | VARCHAR(255) | VARCHAR(255)	| VARCHAR(255)	|DATE				|	
//	<PK>			|<PK>		  |


@Entity
@Table(name="OPERATIONS")
public class OperationEntity {

//	private String operationSpace;
//	private String operationId;
	
	@EmbeddedId
	private OperationIdPK operationIdPK;
	private String itemSpace;
	private String itemId;
	private String userSpace;
	private String userEmail;
	private String type;
	private Date createdTimestamp;
	@Transient
	private Map<String, Object> operationAttributes;

	public OperationEntity() {
	}

	public OperationEntity(OperationIdPK operationIdPK, String itemSpace, String itemId, String userSpace,
			String userEmail, String type, Date createdTimestamp, Map<String, Object> operationAttributes) {
		super();
//		this.operationSpace = operationSpace;
//		this.operationId = operationId;

		this.operationIdPK = operationIdPK;
		this.itemSpace = itemSpace;
		this.itemId = itemId;
		this.userSpace = userSpace;
		this.userEmail = userEmail;
		this.type = type;
		this.createdTimestamp = createdTimestamp;
		this.operationAttributes = operationAttributes;
	}

//	@Id
//	public String getOperationSpace() {
//		return operationSpace;
//	}
//
//	public void setOperationSpace(String operationSpace) {
//		this.operationSpace = operationSpace;
//	}

//	@Id
//	public String getOperationId() {
//		return operationId;
//	}
//
//	public void setOperationId(String operationId) {
//		this.operationId = operationId;
//	}
	
	
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

	public Map<String, Object> getOperationAttributes() {
		return operationAttributes;
	}

	public void setOperationAttributes(Map<String, Object> operationAttributes) {
		this.operationAttributes = operationAttributes;
	}

}
