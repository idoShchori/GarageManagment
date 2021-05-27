package twins.logic.logicImplementation.useCases;

import java.util.List;

import org.springframework.stereotype.Service;

import twins.data.UserRole;
import twins.items.ItemIdBoundary;
import twins.logic.Exceptions.EmptyFieldsException;
import twins.logic.Exceptions.IllegalItemTypeException;
import twins.operations.OperationBoundary;
import twins.users.UserBoundary;
import twins.users.UserId;

@Service
public class GetAllWorkersUseCase extends AbstractUseCase {

	public List<UserBoundary> invoke(OperationBoundary operation, UserRole myRole, int size, int page) {
		UserId userId = operation.getInvokedBy().getUserId();
		
		if (!operation.getOperationAttributes().containsKey("userType"))
			throw new EmptyFieldsException("User type must be specified");
		
		String userType = operation.getOperationAttributes().get("userType").toString();
		if (!validator.isValidUserRole(userType))
			throw new RuntimeException("Illegal user role for report");

		ItemIdBoundary itemId = operation.getItem().getItemId();
		if (!this.itemsService.getSpecificItem(
								userId.getSpace(),
								userId.getEmail(),
								itemId.getSpace(),
								itemId.getId())
				.getType().equals("report")) {
			throw new IllegalItemTypeException("Item's type is not a report");
		}
		
		UserBoundary user = usersService.login(userId.getSpace(), userId.getEmail());
		user.setRole(myRole.toString());
		usersService.updateUser(userId.getSpace(), userId.getEmail(), user);
			
		List<UserBoundary> allUsers =  this.usersService.getAllUsersByRole(UserRole.valueOf(userType), size, page);
		
		user.setRole(UserRole.PLAYER.toString());
		usersService.updateUser(userId.getSpace(), userId.getEmail(), user);
		
		return allUsers;
	}
}
