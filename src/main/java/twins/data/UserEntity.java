package twins.data;

import javax.persistence.EmbeddedId;
//import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.Lob;
import javax.persistence.Table;

//import twins.users.UserId;

//USERS
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
//USER_EMAIL    |USER_SPACE   | USER_ROLE | USER_NAME    | AVATAR       |
//VARCHAR(255)  |VARCHAR(255) |INT        | VARCHAR(255) | VARCHAR(255) | 
//<PK>
@Entity
@Table(name = "USERS")
public class UserEntity {

	private UserIdPK userId;
	private UserRole role;
	private String username;
	private String avatar;

	public UserEntity() {
	}

	@EmbeddedId
	public UserIdPK getUserId() {
		return userId;
	}

	public void setUserId(UserIdPK userId) {
		this.userId = userId;
	}

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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
