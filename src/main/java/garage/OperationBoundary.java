package garage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import garage.users.User;


public class OperationBoundary {

	private OperationId operationId;
	private String type;
	private Item item;
	private Date createdTimestamp;
	private User invokedBy;
	private Map<String, Object> operationAttributes;

	public OperationBoundary() {
		this.operationId = new OperationId();
		this.setType("operationType");
		this.item = new Item();
		this.createdTimestamp = new Date();
		this.invokedBy = new User();
		this.operationAttributes = new HashMap<String, Object>();
	}

	public OperationBoundary(
			OperationId operationId,
			String type,
			Item item,
			Date createdTimestamp,
			User invokedBy,
			Map<String, Object> operationAttributes) {
		
		super();
		this.operationId = operationId;
		this.type = type;
		this.item = item;
		this.createdTimestamp = createdTimestamp;
		this.invokedBy = invokedBy;
		this.operationAttributes = operationAttributes;
	}

	public User getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(User invokedBy) {
		this.invokedBy = invokedBy;
	}

	public OperationId getOperationId() {
		return operationId;
	}

	public void setOperationId(OperationId operationId) {
		this.operationId = operationId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Map<String, Object> getOperationAttributes() {
		return operationAttributes;
	}

	public void setOperationAttributes(Map<String, Object> operationAttributes) {
		this.operationAttributes = operationAttributes;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

}
