package twins.data;

//import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
//import javax.persistence.Lob;
import javax.persistence.Table;

//import twins.users.UserId;


//USERS
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
//USER_EMAIL    |USER_SPACE   | USER_ROLE | USER_NAME    | AVATAR       |
//VARCHAR(255)  |VARCHAR(255) |INT        | VARCHAR(255) | VARCHAR(255) | 
//<PK>
@Entity
@Table(name="USERS")
public class UserEntity {
	
	private String userSpace;
	private String userEmail;
	
	private UserRole role;
	private String username;
	private String avatar;
	
	public UserEntity() {
	}

	
	@Id
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public String getUserSpace() {
		return userSpace;
	}

	public void setUserSpace(String userSpace) {
		this.userSpace = userSpace;
	}

	

//	@Column(name="USER_ROLE")
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

//	@Column(name="AVATAR")
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	
	
}
