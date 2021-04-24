package twins.items;

public class ItemIdBoundary {

	private String space;
	private String id;

	public ItemIdBoundary() {
		this.setSpace("2021b.twins");
		this.setId("46");
	}

	public ItemIdBoundary(String space, String id) {
		super();
		this.space = space;
		this.id = id;
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

