package twins.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import twins.data.OperationEntity;
import twins.operations.OperationBoundary;

@Service
public class OperationsServiceMockup implements OperationsService {
	private Map<String, OperationEntity> operations;
	private EntityConverter entityConverter;
	
	public OperationsServiceMockup() {
		// thread-safe map
		this.operations = Collections.synchronizedMap(new HashMap<>());
	}
	
	
	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}

	@Override
	public Object invokeOperation(OperationBoundary operation) {
		OperationEntity entity = this.entityConverter.toEntity(operation);

		//	TODO: add operation id and space, update date, and add to the map
		return null;
	}

	@Override
	public OperationBoundary invokeAsynchronous(OperationBoundary operation) {
//		TODO: add operation id and space, update date, and add to the map
		return null;
	}

	@Override
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {
		return this.operations
				.values()
				.stream()
				.map(this.entityConverter::toBoundary)	// convert to Stream<OperationBoundary>
				.collect(Collectors.toList());			//	convert to List<OperationBoundary>
	}

	@Override
	public void deleteAllOperations(String adminSpace, String adminEmail) {
		this.operations.clear();
	}

}
