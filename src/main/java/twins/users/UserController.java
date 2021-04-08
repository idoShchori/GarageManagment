package twins.users;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import twins.logic.logicImplementation.UsersServiceMockup;

@RestController
public class UserController {

	private UsersServiceMockup userService;
	
	@Autowired
	public void setUserServiceMockup(UsersServiceMockup userService) {
		this.userService = userService;
	}
	
	@RequestMapping(
			path = "/twins/users",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createNewUser(@RequestBody NewUserDetails input) {
		// STUB IMPLEMENTATION
		UserBoundary user = new UserBoundary();
		user.setUserId(new UserId("2021b.twins", input.getEmail()));
		user.setRole(input.getRole());
		user.setUsername(input.getUsername());
		user.setAvatar(input.getAvatar());

		return this.userService.createUser(user);
	}

	@RequestMapping(
			path = "/twins/users/login/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary loginUser(
			@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {
//		// STUB implementation
//		UserBoundary user = new UserBoundary(new UserId(space, email));
//		user.setRole(UserRole.valueOf("MANAGER"));
//		user.setUsername("Demo User");
//		user.setAvatar("J");

		return this.userService.login(space, email);
	}

	@RequestMapping(
			path = "/twins/users/{userSpace}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUserDetails(@PathVariable("userSpace") String space, @PathVariable("userEmail") String email,
			@RequestBody UserBoundary update) {
//		// STUB implementation - do nothing
//		System.err.println("(STUB) successfully written data to database ---> " + space + " , " + email);
		this.userService.updateUser(space, email, update);
	}
}
