package twins.logic.logicImplementation.jpa;

import twins.operations.OperationBoundary;
import twins.users.UserBoundary;
import twins.users.UserId;

public interface Validatorable {
	public boolean isValidEmail(String email);
	
	public boolean isValidUser(UserBoundary user);
	
	public boolean isValidUserRole(String role);
	
	public boolean isValidUserId(UserId uid);
	
	public boolean isValidOperation(OperationBoundary operation);
}
