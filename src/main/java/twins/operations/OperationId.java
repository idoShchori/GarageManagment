package twins.operations;

public class OperationId {
	private String space;
	private String id;

	public OperationId() {
		this.setSpace("2021b.twins");
		this.setId("535");
	}
	
	public OperationId(String space, String id) {
		this.setSpace(space);
		this.setId(id);
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
