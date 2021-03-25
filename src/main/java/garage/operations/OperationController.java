package garage.operations;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OperationController {

	@RequestMapping(
			path = "/twins/operations",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Object invokeOperationOnItem(@RequestBody OperationBoundary input) {
		// STUB IMPLEMENTATION

		OperationBoundary out = new OperationBoundary();
		out.getOperationAttributes().put("Stub Example", "Attribute");
		return out;
	}

	@RequestMapping(
			path = "/twins/operations/async",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary invokeOperationOnItemAsync(@RequestBody OperationBoundary input) {
		// STUB IMPLEMENTATION

		input.setOperationId(new OperationId());

		return input;
	}

}
