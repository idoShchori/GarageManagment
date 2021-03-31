package twins.operations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twins.logic.OperationsService;
import twins.users.UserBoundary;
import twins.users.UserId;

@RestController
public class OperationController {
	private OperationsService operationsService;
	
	
	@Autowired
	public OperationController(OperationsService os) {
		super();
		this.operationsService = os;
	}
	

	@RequestMapping(
			path = "/twins/operations",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Object invokeOperationOnItem(@RequestBody OperationBoundary input) {
		return this.operationsService.invokeOperation(input);
	}

	
	@RequestMapping(
			path = "/twins/operations/async",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary invokeOperationOnItemAsync(@RequestBody OperationBoundary input) {
		return this.operationsService.invokeAsynchronous(input);
	}

	
	@RequestMapping(
			path = "/twins/operations/",
			method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary[] getAllOperations(@RequestBody UserBoundary admin) {
		
		UserId userId = admin.getUserId();
		
		List<OperationBoundary> allOperations = 
				this.operationsService
				.getAllOperations(userId.getSpace(), userId.getEmail());
		
		return allOperations
				.toArray(new OperationBoundary[0]);
	}
	
	
	@RequestMapping(
			path = "/twins/operations/",
			method = RequestMethod.DELETE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void deleteAllOperations(@RequestBody  UserBoundary admin) {
		
		UserId userId = admin.getUserId();
		
		this.operationsService
			.deleteAllOperations(userId.getSpace(), userId.getEmail());
	}

}
