package twins.data;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class OperationIdPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String operationSpace;
	private String operationId;
	
	
	public OperationIdPK() {
		super();
	}

	public OperationIdPK(String operationSpace, String operationId) {
		super();
		this.operationSpace = operationSpace;
		this.operationId = operationId;
	}

	public String getOperationSpace() {
		return operationSpace;
	}


	public void setOperationSpace(String operationSpace) {
		this.operationSpace = operationSpace;
	}


	public String getOperationId() {
		return operationId;
	}


	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	@Override
	public String toString() {
		return operationSpace + "@@" + operationId;
	}
	
	
	
}
