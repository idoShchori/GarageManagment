package twins.users;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twins.logic.logicImplementation.UserServiceJpa;
//import twins.logic.logicImplementation.UsersServiceMockup;

@RestController
public class UserController {

	private UserServiceJpa userService;
	
	@Autowired
	public void setUserServiceJpa(UserServiceJpa userService) {
		this.userService = userService;
	}
	
	@RequestMapping(
			path = "/twins/users",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createNewUser(@RequestBody NewUserDetails input) {
		return this.userService.createUser(new UserBoundary(input));
	}

	@RequestMapping(
			path = "/twins/users/login/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary loginUser(
			@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {
		return this.userService.login(space, email);
	}

	@RequestMapping(
			path = "/twins/users/{userSpace}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUserDetails(@PathVariable("userSpace") String space, @PathVariable("userEmail") String email,
			@RequestBody UserBoundary update) {
		this.userService.updateUser(space, email, update);
	}
	
}
