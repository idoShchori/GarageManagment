package twins.logic.logicImplementation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import twins.data.UserEntity;
import twins.data.UserRole;
import twins.logic.UsersService;
import twins.logic.logicImplementation.useCases.FixVehicleUseCase;
import twins.operations.OperationBoundary;
import twins.users.UserId;

@Component
public class AsyncOperationHandler {
	private ObjectMapper jackson;
	private EntityConverter entityConverter;
	private UsersService usersService;
	private FixVehicleUseCase fixVehicle;
	
	
	private UserRole validOperationRole = UserRole.PLAYER;

	public AsyncOperationHandler() {
		this.jackson = new ObjectMapper();
	}

	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}
	
	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	
	@Autowired
	public void setFixVehicle(FixVehicleUseCase fixVehicle) {
		this.fixVehicle = fixVehicle;
	}
	
	@Transactional
	@JmsListener(destination = "asyncInbox")
	public void handleJson(String json) {
		OperationBoundary boundary;
		try {
			boundary = this.jackson.readValue(json, OperationBoundary.class);
		} catch (Exception e) {
			throw new RuntimeException("Error executing operation");
		}
		
		UserId userId = boundary.getInvokedBy().getUserId();
		UserEntity user = entityConverter.toEntity(usersService.login(userId.getSpace(), userId.getEmail()));
		UserRole actualRole = user.getRole();
		
		user.setRole(validOperationRole);
		usersService.updateUser(
				userId.getSpace(),
				userId.getEmail(),
				entityConverter.toBoundary(user));
		
		this.fixVehicle.invoke(boundary);
		
		user.setRole(actualRole);
		usersService.updateUser(
				userId.getSpace(),
				userId.getEmail(),
				entityConverter.toBoundary(user));
	}
}
