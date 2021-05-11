package twins.logic.logicImplementation.jpa;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import twins.data.OperationEntity;
import twins.data.OperationIdPK;
import twins.data.dao.OperationsDao;
import twins.logic.OperationsService;
import twins.logic.logicImplementation.EntityConverter;
import twins.logic.logicImplementation.Validator;
import twins.operations.OperationBoundary;

@Service
public class OperationsServiceJpa implements OperationsService {

	private OperationsDao operationsDao;
	private EntityConverter entityConverter;
	private Validator validator;
	private String springApplicatioName;

	@Value("${spring.application.name:defaultName}")
	public void setSpringApplicatioName(String springApplicatioName) {
		this.springApplicatioName = springApplicatioName;
	}

	@Autowired
	public void setOperationsDao(OperationsDao operationsDao) {
		this.operationsDao = operationsDao;
	}

	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

//	public boolean validateOperationFields(OperationBoundary operation) {
//		UserId userId = operation.getInvokedBy().getUserId();
//		if (userId == null)
//			throw new EmptyFieldsException("An operation must be performed by a valid user");
//
//		if (userId.getEmail() == null || userId.getEmail().isEmpty())
//			throw new EmptyFieldsException("Invalid user's `Email`");
//		else if (validem)
//		
//		if (userId.getSpace() == null || userId.getSpace().isEmpty())
//			throw new EmptyFieldsException("Invalid user's `Email` or `Space`");
//
//		ItemIdBoundary itemId = operation.getItem().getItemId();
//		if (itemId == null)
//			throw new EmptyFieldsException("An operation must be performed on a valid item");
//
//		if (itemId.getId() == null || itemId.getSpace() == null ||
//				itemId.getId().isEmpty() || itemId.getSpace().isEmpty())
//			throw new EmptyFieldsException("Invalid item's `Id` or `Space`");
//
//		if (operation.getType() == null || operation.getType().isEmpty())
//			throw new EmptyFieldsException("Type of operation must be specified");
//		
//		return true;
//	}

	@Override
	@Transactional(readOnly = false)
	public Object invokeOperation(OperationBoundary operation) {

		validator.isValidOperation(operation);

		OperationEntity entity = this.entityConverter.toEntity(operation);

		OperationIdPK pk = new OperationIdPK(this.springApplicatioName, UUID.randomUUID().toString());
		entity.setOperationIdPK(pk);
		entity.setCreatedTimestamp(new Date());

		this.operationsDao.save(entity);

		return this.entityConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public OperationBoundary invokeAsynchronous(OperationBoundary operation) {

		validator.isValidOperation(operation);

		OperationEntity entity = this.entityConverter.toEntity(operation);

		OperationIdPK pk = new OperationIdPK(this.springApplicatioName, UUID.randomUUID().toString());
		entity.setOperationIdPK(pk);
		entity.setCreatedTimestamp(new Date());

		this.operationsDao.save(entity);

		return this.entityConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {

		// TODO: validate that `UserRole` == ADMIN, if not -> throws an exception

		Iterable<OperationEntity> allEntities = this.operationsDao.findAll();

		return StreamSupport.stream(allEntities.spliterator(), false).map(this.entityConverter::toBoundary) // convert
																											// to
																											// Stream<OperationBoundary>
				.collect(Collectors.toList()); // convert to List<OperationBoundary>
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllOperations(String adminSpace, String adminEmail) {

		// TODO: validate that `UserRole` == ADMIN, if not -> throws an exception

		this.operationsDao.deleteAll();
	}

}