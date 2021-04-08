package twins.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twins.data.UserRole;
import twins.logic.ItemsService;
import twins.logic.OperationsService;
import twins.logic.UsersService;
import twins.operations.OperationBoundary;
import twins.users.UserBoundary;
import twins.users.UserId;




@RestController
public class AdminController {
	
	private UsersService userService;
	private OperationsService operationService;
	private ItemsService itemService;
	
	
	@Autowired
	public AdminController(UsersService userService,OperationsService operationService,
			ItemsService itemService ) {
		this.userService = userService;
		this.operationService = operationService;
		this.itemService = itemService;
	}
	@RequestMapping(
			path = "/twins/admin/users/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsersInSpace(@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {
		// STUB IMPLEMENTATION
		System.out.println("all users deleted");
		this.userService.deleteAllUsers(space, email);
	}

	@RequestMapping(
			path = "/twins/admin/items/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllItemsInSpace(@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {
		// STUB IMPLEMENTATION.
		System.out.println("all items deleted");
		this.itemService.deleteAllItems(space, email);
	}

	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllOperationsInSpace(@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {
		// STUB IMPLEMENTATION.
		System.out.println("all operations deleted");
		this.operationService.deleteAllOperations(space, email);
	}

	@RequestMapping(
			path = "/twins/admin/users/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllUsers(@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {
		return this.userService.getAllUsers(space, email).toArray(new UserBoundary[0]);
	}
	

	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary[] exportAllOperations(@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {
		
		List<OperationBoundary> allOperations = 
				this.operationService
				.getAllOperations(space,email);
		
		return allOperations
				.toArray(new OperationBoundary[0]);
	}
	
	//TODO: get Items of all USERS


}
