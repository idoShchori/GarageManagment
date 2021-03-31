package twins.logic;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
		
		if (operation.getInvokedBy().getUserId() == null)
			throw new RuntimeException("An operation must be performed by a valid user");
		
		if (operation.getItem().getItemId() == null)
			throw new RuntimeException("An operation must be performed on a valid item");

		OperationEntity entity = this.entityConverter.toEntity(operation);
		
		entity.setOperationSpace("2021b@guy.kabiri");
		entity.setOperationId(UUID.randomUUID().toString());
		entity.setCreatedTimestamp(new Date());
		
		String currId = entity.getOperationSpace().concat('/' + entity.getOperationId());
		
		this.operations.put(currId, entity);
		
		return currId;
	}

	@Override
	public OperationBoundary invokeAsynchronous(OperationBoundary operation) {

		if (operation.getInvokedBy().getUserId() == null)
			throw new RuntimeException("An operation must be performed by a valid user");
		
		if (operation.getItem().getItemId() == null)
			throw new RuntimeException("An operation must be performed on a valid item");

		OperationEntity entity = this.entityConverter.toEntity(operation);
		
		entity.setOperationSpace("2021b@guy.kabiri");
		entity.setOperationId(UUID.randomUUID().toString());
		entity.setCreatedTimestamp(new Date());
		
		String currId = entity.getOperationSpace().concat(entity.getOperationId());
		
		this.operations.put(currId, entity);
		
		return this.entityConverter.toBoundary(entity);
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
