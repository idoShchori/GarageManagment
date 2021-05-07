package twins.logic.logicImplementation.jpa;

import java.util.regex.Pattern;

import twins.data.UserRole;
import twins.items.ItemIdBoundary;
import twins.logic.Exceptions.EmptyFieldsException;
import twins.operations.OperationBoundary;
import twins.users.UserBoundary;
import twins.users.UserId;

public class Validator implements Validatorable {

	@Override
	public boolean isValidEmail(String email) {
		String regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern pat = Pattern.compile(regex);
		return pat.matcher(email).matches();
	}

	@Override
	public boolean isValidUser(UserBoundary user) {
		if (user.getUserId().getEmail() == null || user.getUserId().getEmail().isEmpty()) {
			throw new EmptyFieldsException("UserEmail must be specified!");
		} else if (!isValidEmail(user.getUserId().getEmail())) {
			throw new EmptyFieldsException("UserEmail must be Valid!");
		}

		if (user.getUsername() == null) {
			throw new EmptyFieldsException("UserName must be specified!");
		}
		if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
			throw new EmptyFieldsException("UserAvatar must be specified!");
		}
		if (user.getRole() == null) {
			throw new EmptyFieldsException("UserRole must be specified!");
		}

		try {
			UserRole.valueOf(user.getRole());
		} catch (Exception e) {
			throw new EmptyFieldsException("UserRole must be PLAYER/MANGAER/ADMIN");
		}
		
		return true;
	}

	@Override
	public boolean isValidUserRole(String role) {
		try {
			UserRole.valueOf(role);
			return true;
		} catch (Exception e) {
			throw new EmptyFieldsException("UserRole must be PLAYER/MANGAER/ADMIN");
		}
	}

	@Override
	public boolean isValidUserId(UserId uid) {
		if (uid.getEmail() == null || uid.getEmail().isEmpty()) {
			throw new EmptyFieldsException("UserEmail must be specified!");
			
		} else if (!isValidEmail(uid.getEmail())) {
			throw new EmptyFieldsException("UserEmail must be Valid!");
		}
		return true;
	}

	@Override
	public boolean isValidOperation(OperationBoundary operation) {
		UserId userId = operation.getInvokedBy().getUserId();
			
		isValidUserId(userId);
		
		if (userId.getSpace() == null || userId.getSpace().isEmpty())
			throw new EmptyFieldsException("Invalid user's `Email` or `Space`");

		ItemIdBoundary itemId = operation.getItem().getItemId();
		if (itemId == null)
			throw new EmptyFieldsException("An operation must be performed on a valid item");

		if (itemId.getId() == null || itemId.getSpace() == null ||
				itemId.getId().isEmpty() || itemId.getSpace().isEmpty())
			throw new EmptyFieldsException("Invalid item's `Id` or `Space`");

		if (operation.getType() == null || operation.getType().isEmpty())
			throw new EmptyFieldsException("Type of operation must be specified");
		
		return true;
	}

}
