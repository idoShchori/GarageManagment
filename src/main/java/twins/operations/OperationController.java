package twins.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import twins.logic.OperationsService;

@RestController
public class OperationController {
	private OperationsService operationsService;

	@Autowired
	public OperationController(OperationsService os) {
		super();
		this.operationsService = os;
	}

	@RequestMapping(path = "/twins/operations", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object invokeOperationOnItem(@RequestBody OperationBoundary input,
			@RequestParam(name = "size", required = false, defaultValue = "20") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.operationsService.invokeOperation(input,page,size);
	}

	@RequestMapping(path = "/twins/operations/async", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary invokeOperationOnItemAsync(@RequestBody OperationBoundary input) {
		return this.operationsService.invokeAsynchronous(input);
	}

}
