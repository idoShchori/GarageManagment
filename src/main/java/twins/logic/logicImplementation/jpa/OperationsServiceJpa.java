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
import twins.logic.UpdatedItemsService;
import twins.logic.OperationsService;
import twins.logic.UsersService;
import twins.logic.Exceptions.IllegalOperationType;
import twins.logic.Exceptions.ItemNotFoundException;
import twins.logic.Exceptions.UserAccessDeniedException;
import twins.logic.logicImplementation.EntityConverter;
import twins.logic.logicImplementation.Validator;
import twins.logic.logicImplementation.useCases.BestWorkerUseCase;
import twins.logic.logicImplementation.useCases.FixVehicleUseCase;
import twins.logic.logicImplementation.useCases.GetAllWorkersUseCase;
import twins.logic.logicImplementation.useCases.GetMaintenancesByDateUseCase;
import twins.logic.logicImplementation.useCases.GetRevenueReportUseCase;
import twins.logic.logicImplementation.useCases.PendingMaintenanceListUseCase;
import twins.logic.logicImplementation.useCases.UseCase;
import twins.operations.OperationBoundary;

@Service
public class OperationsServiceJpa implements OperationsService {

	private OperationsDao operationsDao;
	private UsersService usersService;
	private UpdatedItemsService itemService;
	private EntityConverter entityConverter;
	private Validator validator;
	private String springApplicatioName;
	private JmsTemplate jmsTemplate;
	private FixVehicleUseCase fixVehicle;
	private GetMaintenancesByDateUseCase getMaintenancesByDate;
	private GetAllWorkersUseCase getAllWorkers;
	private GetRevenueReportUseCase getRevenueReport;
	private PendingMaintenanceListUseCase pendingMaintenance;
	private BestWorkerUseCase bestWorker;

	
	private UserRole validOperationRole = UserRole.PLAYER;

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
	public void setItemsService(UpdatedItemsService itemsService) {
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
	public void setFixVehicle(FixVehicleUseCase fixVehicle) {
		this.fixVehicle = fixVehicle;
	}

	@Autowired
	public void setGetMaintenancesByDate(GetMaintenancesByDateUseCase getMaintenancesByDate) {
		this.getMaintenancesByDate = getMaintenancesByDate;
	}

	@Autowired
	public void setGetAllWorkers(GetAllWorkersUseCase getAllWorkers) {
		this.getAllWorkers = getAllWorkers;
	}
	
	@Autowired
	public void setGetRevenueReport(GetRevenueReportUseCase getRevenueReport) {
		this.getRevenueReport = getRevenueReport;
	}

	@Autowired
	public void setPendingMaintenance(PendingMaintenanceListUseCase pendingMaintenance) {
		this.pendingMaintenance = pendingMaintenance;
	}
	
	@Autowired
	public void setBestWorkerUseCase(BestWorkerUseCase bestWorker) {
		this.bestWorker = bestWorker;
	}

	@Override
	@Transactional(readOnly = false)
	public Object invokeOperation(OperationBoundary operation) {

		validator.isValidOperation(operation);

		OperationEntity entity = this.entityConverter.toEntity(operation);
		
		UserIdPK userId = new UserIdPK(entity.getUserSpace(), entity.getUserEmail());
		
//		if user does not exits, exception will be thrown inside this method
		UserEntity user = this.entityConverter.toEntity(this.usersService.login(userId.getSpace(), userId.getEmail()));
		
		if(!operation.getOperationAttributes().containsKey("operationName"))
			throw new IllegalOperationType("Type of operation must be specified");
		
		String opName = (String) operation.getOperationAttributes().get("operationName");
		validator.isValidUseCase(opName);
		UseCase operationCase = UseCase.valueOf(opName);

		if (!validator.isValidUserForUseCase(user, operationCase))
			throw new UserAccessDeniedException("This user can not perform this usecase");
		
		UserRole actualRole = user.getRole();
				
		user.setRole(validOperationRole);
		usersService.updateUser(
				userId.getSpace(),
				userId.getEmail(),
				entityConverter.toBoundary(user));
		
		ItemIdPK itemId = new ItemIdPK(entity.getItemSpace(), entity.getItemId());
		ItemBoundary item = this.itemService.getSpecificItem(userId.getSpace(), userId.getEmail(), itemId.getSpace(), itemId.getId());
		
		if (!item.isActive())
			throw new ItemNotFoundException("Item does not exist");	

		OperationIdPK pk = new OperationIdPK(this.springApplicatioName, UUID.randomUUID().toString());
		entity.setOperationIdPK(pk);
		entity.setCreatedTimestamp(new Date());
		
		int size = 20, page = 0;
		
		try {
			size = Integer.parseInt(operation.getOperationAttributes().get("size").toString());
			page = Integer.parseInt(operation.getOperationAttributes().get("page").toString());
		} catch (Exception e) {
			// return to default in case of exception
			size = 20;
			page = 0;
		}
		
		Object returnedValue = null;
		
		switch (operationCase) {
		case FIX_VEHICLE:
			this.fixVehicle.invoke(operation);
			break;
		case MAINTENANCE_BY_DATE:
			returnedValue = this.getMaintenancesByDate.invoke(operation, size, page);
			break;
		case GET_ALL_WORKERS:
			returnedValue = this.getAllWorkers.invoke(operation, actualRole, size, page);
			break;
		case GET_REVENUE_REPORT:
			returnedValue = this.getRevenueReport.invoke(operation);
			break;
		case PENDING_MAINTENANCE:
			returnedValue = this.pendingMaintenance.invoke(operation, size, page);
			break;
		case BEST_WORKER:
			returnedValue = this.bestWorker.invoke(operation);
			break;
		default:
			throw new IllegalOperationType("Illegal operation type");
		}
		
		user.setRole(actualRole);
		usersService.updateUser(
				userId.getSpace(),
				userId.getEmail(),
				entityConverter.toBoundary(user));
		
		this.operationsDao.save(entity);
		
		if (returnedValue != null)
			return returnedValue;
		
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

		if(!operation.getOperationAttributes().containsKey("operationName"))
			throw new IllegalOperationType("Type of operation must be specified");
		
		String opName = (String) operation.getOperationAttributes().get("operationName");
		validator.isValidUseCase(opName);
		UseCase operationCase = UseCase.valueOf(opName);

		if (!validator.isValidUserForUseCase(user, operationCase))
			throw new UserAccessDeniedException("This user can not perform this usecase");
		
		ItemIdPK itemId = new ItemIdPK(entity.getItemSpace(), entity.getItemId());
		ItemBoundary item = this.itemService.getSpecificItem(userId.getSpace(), userId.getEmail(), itemId.getSpace(), itemId.getId());
		
		if (!item.isActive())
			throw new ItemNotFoundException("Item does not exist");	

		OperationIdPK pk = new OperationIdPK(this.springApplicatioName, UUID.randomUUID().toString());
		entity.setOperationIdPK(pk);
		entity.setCreatedTimestamp(new Date());
		
		if (operationCase != UseCase.FIX_VEHICLE)
			throw new RuntimeException("This operation can not be performed a-syncronized");
		
		this.operationsDao.save(entity);
		
		ObjectMapper jackson = new ObjectMapper();
		try {
			String json = jackson.writeValueAsString(operation);
			this.jmsTemplate.send("asyncInbox", session -> session.createTextMessage(json));			
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return this.entityConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	@Deprecated
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {
		throw new RuntimeException("Deprecated method");

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

		// if user does not exits, exception will be thrown inside this method
		UserEntity user = this.entityConverter.toEntity(this.usersService.login(userId.getSpace(), userId.getEmail()));
		if (!validator.isUserRole(user, UserRole.ADMIN))
			throw new UserAccessDeniedException("User's role is not admin");

		return this.operationsDao
				.findAll(PageRequest.of(page, size, Direction.DESC, "createdTimestamp", "operationIdPK")).getContent()
				.stream().map(this.entityConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllOperations(String adminSpace, String adminEmail) {

		UserIdPK userId = new UserIdPK(adminSpace, adminEmail);

		// if user does not exits, exception will be thrown inside this method
		UserEntity user = this.entityConverter.toEntity(this.usersService.login(userId.getSpace(), userId.getEmail()));
		if (!validator.isUserRole(user, UserRole.ADMIN))
			throw new UserAccessDeniedException("User's role is not admin");

		this.operationsDao.deleteAll();
	}
}