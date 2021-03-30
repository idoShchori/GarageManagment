package twins.users;

public class UserId {

	private String space;
	private String email;

	public UserId() {

		//		stub implementation
		this.setSpace("2020b.twins");
		this.setEmail("myyy@email.com");
	}

	public UserId(String space, String email) {
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
