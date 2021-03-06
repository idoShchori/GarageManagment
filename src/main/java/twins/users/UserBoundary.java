package twins.users;



public class UserBoundary {

	private UserId userId;
	private String role;
	private String username;
	private String avatar;

	public UserBoundary() {
	}

	public UserBoundary(UserId userId) {
		this();
		this.userId = userId;
	}
	
	public UserBoundary(NewUserDetails input) {
		this.setUserId(new UserId(null, input.getEmail()));
		this.setRole(input.getRole());
		this.setUsername(input.getUsername());
		this.setAvatar(input.getAvatar());
	}
	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
