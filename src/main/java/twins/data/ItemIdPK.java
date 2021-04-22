package twins.data;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ItemIdPK implements Serializable{
	private static final long serialVersionUID = 1L;

	
	private String space;
	private String id;
	
	public ItemIdPK(String space, String id) {
		super();
		this.space = space;
		this.id = id;
	}

	public ItemIdPK() {}
	
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
