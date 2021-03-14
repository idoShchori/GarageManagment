package garage;

public class User {
	private UserId userId;
	
	public User() {
		this.userId = new UserId();
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}
	
}
