package garage;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@RequestMapping(
			path = "/twins/users",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary invokeOperationOnItem(@RequestBody newUserDetails input) {
		// STUB IMPLEMENTATION
		UserBoundary user = new UserBoundary();
		user.setUserId(new UserId("2021b.twins", input.getEmail()));
		user.setRole(input.getRole());
		user.setUserName(input.getUserName());
		user.setAvatar(input.getAvatar());

		return user;
	}

	@RequestMapping(
			path = "/twins/users/login/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary getSingleMessage(@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {
		// STUB implementation
		UserBoundary user = new UserBoundary(new UserId(space, email));
		user.setRole("MANAGER");
		user.setUserName("Demo User");
		user.setAvatar("J");

		return user;
	}

	@RequestMapping(
			path = "/twins/users/{userSpace}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateData(@PathVariable("userSpace") String space, @PathVariable("userEmail") String email,
			@RequestBody UserBoundary update) {
		// STUB implementation - do nothing
		System.err.println("(STUB) successfully written data to database ---> " + space + " , " + email);
	}
}
