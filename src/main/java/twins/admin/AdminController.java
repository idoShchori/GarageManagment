package twins.admin;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twins.operations.OperationBoundary;
import twins.users.UserBoundary;
import twins.users.UserId;


@RestController
public class AdminController {
	@RequestMapping(
			path = "/twins/admin/users/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsersInSpace(@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {
		// STUB IMPLEMENTATION
		System.out.println("all users deleted");
	}

	@RequestMapping(
			path = "/twins/admin/items/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllItemsInSpace(@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {
		// STUB IMPLEMENTATION.
		System.out.println("all items deleted");
	}

	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllOperationsInSpace(@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {
		// STUB IMPLEMENTATION.
		System.out.println("all operations deleted");
	}

	@RequestMapping(
			path = "/twins/admin/users/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllUsers() {
		// STUB IMPLEMENTATION
		UserBoundary users[] = new UserBoundary[1];
		users[0] = new UserBoundary();
		users[0].setUserId(new UserId("2021b.twins", "test@test.com"));
		users[0].setRole("admin");
		users[0].setUsername("admin");
		users[0].setAvatar("admin");

		return users;
	}

	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary[] exportAllOperations(@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {
		// STUB IMPLEMENTATION
		OperationBoundary operations[] = new OperationBoundary[1];
		operations[0] = new OperationBoundary();
		operations[0].setType("type");
		return operations;
	}

}
