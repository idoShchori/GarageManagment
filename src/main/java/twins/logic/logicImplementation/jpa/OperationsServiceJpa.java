package twins.logic.logicImplementation.jpa;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import twins.data.OperationEntity;
import twins.data.OperationIdPK;
import twins.data.UserEntity;
import twins.data.UserIdPK;
import twins.data.UserRole;
import twins.data.dao.OperationsDao;
import twins.data.dao.UsersDao;
import twins.logic.OperationsService;
import twins.logic.logicImplementation.EntityConverter;
import twins.logic.logicImplementation.Validator;
import twins.operations.OperationBoundary;

@Service
public class OperationsServiceJpa implements OperationsService {

	private OperationsDao operationsDao;
	private UsersDao userDao;
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
	public void setUsersDao(UsersDao userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@Override
	@Transactional(readOnly = false)
	public Object invokeOperation(OperationBoundary operation) {

		validator.isValidOperation(operation);

		OperationEntity entity = this.entityConverter.toEntity(operation);
		
		UserIdPK userId = new UserIdPK(entity.getUserSpace(), entity.getUserEmail());
		if (!this.userDao.existsById(userId))
			throw new RuntimeException("User does not exist");
		
		UserEntity user = this.userDao.findById(userId).get();
		if (!validator.isUserRole(user, UserRole.PLAYER))
			throw new RuntimeException("User's role is not player");

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
		
		UserIdPK userId = new UserIdPK(entity.getUserSpace(), entity.getUserEmail());
		if (!this.userDao.existsById(userId))
			throw new RuntimeException("User does not exist");
		
		UserEntity user = this.userDao.findById(userId).get();
		if (!validator.isUserRole(user, UserRole.PLAYER))
			throw new RuntimeException("User's role is not player");

		OperationIdPK pk = new OperationIdPK(this.springApplicatioName, UUID.randomUUID().toString());
		entity.setOperationIdPK(pk);
		entity.setCreatedTimestamp(new Date());

		this.operationsDao.save(entity);

		return this.entityConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	@Deprecated
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {
		throw new RuntimeException("Deprecated method");
		
//		// TODO: validate that `UserRole` == ADMIN, if not -> throws an exception
//
//		Iterable<OperationEntity> allEntities = this.operationsDao.findAll();
//
//		return StreamSupport.stream(allEntities.spliterator(), false).map(this.entityConverter::toBoundary) // convert
//																											// to
//																											// Stream<OperationBoundary>
//				.collect(Collectors.toList()); // convert to List<OperationBoundary>
	}
	
	@Override
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail, int size, int page) {
		
		UserIdPK userId = new UserIdPK(adminSpace, adminEmail);
		if (!this.userDao.existsById(userId))
			throw new RuntimeException("User does not exist");
		
		UserEntity user = this.userDao.findById(userId).get();
		if (!validator.isUserRole(user, UserRole.ADMIN))
			throw new RuntimeException("User's role is not admin");
		
		return this.operationsDao
				.findAll(PageRequest.of(page, size, Direction.DESC, "createdTimestamp", "operationIdPK"))
				.getContent()
				.stream()
				.map(this.entityConverter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllOperations(String adminSpace, String adminEmail) {

		UserIdPK userId = new UserIdPK(adminSpace, adminEmail);
		if (!this.userDao.existsById(userId))
			throw new RuntimeException("User does not exist");
		
		UserEntity user = this.userDao.findById(userId).get();
		if (!validator.isUserRole(user, UserRole.ADMIN))
			throw new RuntimeException("User's role is not admin");

		this.operationsDao.deleteAll();
	}
}