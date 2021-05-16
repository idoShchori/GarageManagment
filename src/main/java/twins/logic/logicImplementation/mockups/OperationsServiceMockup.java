package twins.logic.logicImplementation.mockups;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import twins.data.OperationEntity;
import twins.data.OperationIdPK;
import twins.logic.OperationsService;
import twins.logic.logicImplementation.EntityConverter;
import twins.operations.OperationBoundary;

//@Service
public class OperationsServiceMockup implements OperationsService {
	private Map<String, OperationEntity> operations;
	private EntityConverter entityConverter;
	private String springApplicatioName;
	
	public OperationsServiceMockup() {
		// thread-safe map
		this.operations = Collections.synchronizedMap(new HashMap<>());
	}
	
	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}
	
	@Value("${spring.application.name:defaultName}")
	public void setSpringApplicatioName(String springApplicatioName) {
		this.springApplicatioName = springApplicatioName;
	}

	@Override
	public Object invokeOperation(OperationBoundary operation, int page, int size) {
		
		if (operation.getInvokedBy().getUserId() == null)
			throw new RuntimeException("An operation must be performed by a valid user");
		
		if (operation.getItem().getItemId() == null)
			throw new RuntimeException("An operation must be performed on a valid item");

		OperationEntity entity = this.entityConverter.toEntity(operation);
		
		OperationIdPK pk = new OperationIdPK(this.springApplicatioName, UUID.randomUUID().toString());
		entity.setOperationIdPK(pk);
		entity.setCreatedTimestamp(new Date());
		
		String currId = entity.getOperationIdPK().toString();
		
		this.operations.put(currId, entity);
		
		return entity;
	}

	@Override
	public OperationBoundary invokeAsynchronous(OperationBoundary operation) {

		if (operation.getInvokedBy().getUserId() == null)
			throw new RuntimeException("An operation must be performed by a valid user");
		
		if (operation.getItem().getItemId() == null)
			throw new RuntimeException("An operation must be performed on a valid item");

		OperationEntity entity = this.entityConverter.toEntity(operation);
		
		OperationIdPK pk = new OperationIdPK(this.springApplicatioName, UUID.randomUUID().toString());
		entity.setOperationIdPK(pk);
		entity.setCreatedTimestamp(new Date());
		
		String currId = entity.getOperationIdPK().toString();
		
		this.operations.put(currId, entity);
		
		return this.entityConverter.toBoundary(entity);
	}

	@Override
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {
		
		// TODO: validate that `UserRole` == ADMIN, if not -> throws an exception
		
		return this.operations
				.values()
				.stream()
				.map(this.entityConverter::toBoundary)	//	convert to Stream<OperationBoundary>
				.collect(Collectors.toList());			//	convert to List<OperationBoundary>
	}

	@Override
	public void deleteAllOperations(String adminSpace, String adminEmail) {
		
		// TODO: validate that `UserRole` == ADMIN, if not -> throws an exception
		
		this.operations.clear();
	}

	@Override
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

}
