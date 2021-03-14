package garage;

import java.util.Date;

public class OperationBoundary {
	
	private OperationId operationId;
	private String type;
	private Item item;
	private User invokedBy;
	private OperationAttributes operationAttributes;
	private Date createdTimestamp;
	
	public OperationBoundary() {
		this.operationId = new OperationId();
		this.setType("operationType");
		this.item = new Item();
		this.invokedBy = new User();
		this.operationAttributes = new OperationAttributes();
		this.createdTimestamp = new Date();
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

	public OperationAttributes getOperationAttributes() {
		return operationAttributes;
	}

	public void setOperationAttributes(OperationAttributes operationAttributes) {
		this.operationAttributes = operationAttributes;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	
	
}
