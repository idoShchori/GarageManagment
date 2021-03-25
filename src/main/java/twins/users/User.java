package twins.users;


public class User {
	private UserId userId;

	public User() {
		this.userId = new UserId();
	}

	public User(UserId userId) {
		super();
		this.userId = userId;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

}
