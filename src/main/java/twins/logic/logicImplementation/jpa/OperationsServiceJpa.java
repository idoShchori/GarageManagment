package twins.logic.logicImplementation.jpa;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import twins.data.ItemIdPK;
import twins.data.OperationEntity;
import twins.data.OperationIdPK;
import twins.data.UserEntity;
import twins.data.UserIdPK;
import twins.data.UserRole;
import twins.data.dao.OperationsDao;
import twins.items.ItemBoundary;
import twins.logic.ItemsRelationshipService;
import twins.logic.OperationsService;
import twins.logic.UsersService;
import twins.logic.Exceptions.ItemNotFoundException;
import twins.logic.Exceptions.UserAccessDeniedException;
import twins.logic.logicImplementation.EntityConverter;
import twins.logic.logicImplementation.Validator;
import twins.logic.logicImplementation.useCases.FixVehicle;
import twins.logic.logicImplementation.useCases.GetMaintenancesByDate;
import twins.operations.OperationBoundary;

@Service
public class OperationsServiceJpa implements OperationsService {

	private OperationsDao operationsDao;
	private UsersService usersService;
	private ItemsRelationshipService itemService;
	private EntityConverter entityConverter;
	private Validator validator;
	private String springApplicatioName;
	private JmsTemplate jmsTemplate;
	private FixVehicle fixVehicle;
	private GetMaintenancesByDate getMaintenancesByDate;
	
	
	@Value("${spring.application.name:defaultName}")
	public void setSpringApplicatioName(String springApplicatioName) {
		this.springApplicatioName = springApplicatioName;
	}

	@Autowired
	public void setOperationsDao(OperationsDao operationsDao) {
		this.operationsDao = operationsDao;
	}
	
	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	
	@Autowired
	public void setItemsService(ItemsRelationshipService itemsService) {
		this.itemService = itemsService;
	}

	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	@Autowired
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	@Autowired
	public void setFixVehicle(FixVehicle fixVehicle) {
		this.fixVehicle = fixVehicle;
	}
	
	@Autowired
	public void setGetMaintenancesByDate(GetMaintenancesByDate getMaintenancesByDate) {
		this.getMaintenancesByDate = getMaintenancesByDate;
	}

	@Override
	@Transactional(readOnly = false)
	public Object invokeOperation(OperationBoundary operation,int page , int size) {

		validator.isValidOperation(operation);

		OperationEntity entity = this.entityConverter.toEntity(operation);
		
		UserIdPK userId = new UserIdPK(entity.getUserSpace(), entity.getUserEmail());
		
		//		if user does not exits, exception will be thrown inside this method
		UserEntity user = this.entityConverter.toEntity(this.usersService.login(userId.getSpace(), userId.getEmail()));
		if (!validator.isUserRole(user, UserRole.PLAYER))
			throw new UserAccessDeniedException("User's role is not player");
		
		ItemIdPK itemId = new ItemIdPK(entity.getItemSpace(), entity.getItemId());
		ItemBoundary item = this.itemService.getSpecificItem(userId.getSpace(), userId.getEmail(), itemId.getSpace(), itemId.getId());
		
		if (!item.isActive())
			throw new ItemNotFoundException("Item does not exist");	

		OperationIdPK pk = new OperationIdPK(this.springApplicatioName, UUID.randomUUID().toString());
		entity.setOperationIdPK(pk);
		entity.setCreatedTimestamp(new Date());
		
		if(operation.getOperationAttributes().containsKey("operationName")) {
			this.operationsDao.save(entity);
			String opName= (String) operation.getOperationAttributes().get("operationName");
			switch (opName) {
			case "fix vehicle":
				this.fixVehicle.invoke(operation);
				break;
			case "get all maintenances by date":
				return this.getMaintenancesByDate.invoke(operation,page,size);
			default:
				break;
			}
		}

		return this.entityConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public OperationBoundary invokeAsynchronous(OperationBoundary operation) {

		validator.isValidOperation(operation);

		OperationEntity entity = this.entityConverter.toEntity(operation);
		
		UserIdPK userId = new UserIdPK(entity.getUserSpace(), entity.getUserEmail());
		
		//		if user does not exits, exception will be thrown inside this method
		UserEntity user = this.entityConverter.toEntity(this.usersService.login(userId.getSpace(), userId.getEmail()));
		if (!validator.isUserRole(user, UserRole.PLAYER))
			throw new UserAccessDeniedException("User's role is not player");
		
		ItemIdPK itemId = new ItemIdPK(entity.getItemSpace(), entity.getItemId());
		ItemBoundary item = this.itemService.getSpecificItem(userId.getSpace(), userId.getEmail(), itemId.getSpace(), itemId.getId());
		
		if (!item.isActive())
			throw new ItemNotFoundException("Item does not exist");	

		OperationIdPK pk = new OperationIdPK(this.springApplicatioName, UUID.randomUUID().toString());
		entity.setOperationIdPK(pk);
		
		ObjectMapper jackson = new ObjectMapper();
		try {
			String json = jackson.writeValueAsString(operation);
			this.jmsTemplate
				.send("asyncInbox",
						session -> session.createTextMessage(json));
			
			
//			entity.setCreatedTimestamp(new Date());

//			this.operationsDao.save(entity);

			return this.entityConverter.toBoundary(entity);
		} catch (Exception e) {
			throw new RuntimeException();
		}
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
		
		//		if user does not exits, exception will be thrown inside this method
		UserEntity user = this.entityConverter.toEntity(this.usersService.login(userId.getSpace(), userId.getEmail()));
		if (!validator.isUserRole(user, UserRole.ADMIN))
			throw new UserAccessDeniedException("User's role is not admin");
		
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
		
		//		if user does not exits, exception will be thrown inside this method
		UserEntity user = this.entityConverter.toEntity(this.usersService.login(userId.getSpace(), userId.getEmail()));
		if (!validator.isUserRole(user, UserRole.ADMIN))
			throw new UserAccessDeniedException("User's role is not admin");

		this.operationsDao.deleteAll();
	}
}