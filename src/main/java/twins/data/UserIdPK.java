package twins.data;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class UserIdPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4871311189981504510L;
	private String space;
	private String email;

	public UserIdPK() {

	}

	public UserIdPK(String space, String email) {
		this.space = space;
		this.email = email;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
