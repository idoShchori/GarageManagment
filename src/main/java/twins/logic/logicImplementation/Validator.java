package twins.logic.logicImplementation;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import twins.data.UserEntity;
import twins.data.UserRole;
import twins.items.ItemBoundary;
import twins.items.ItemIdBoundary;
import twins.logic.Exceptions.EmptyFieldsException;
import twins.logic.logicImplementation.useCases.UseCase;
import twins.operations.OperationBoundary;
import twins.users.UserBoundary;
import twins.users.UserId;

@Component
public class Validator implements Validatorable {
	
	public Validator() {
	}

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
		isValidItemId(itemId);

		if (operation.getType() == null || operation.getType().isEmpty())
			throw new EmptyFieldsException("Type of operation must be specified");

		return true;
	}

	@Override
	public boolean isValidItem(ItemBoundary item) {
		if (item.getLocation() == null)
			throw new EmptyFieldsException("Item must have a location");

		if (item.getName() == null || item.getName().isEmpty())
			throw new EmptyFieldsException("Item's name is empty");

		if (item.getType() == null || item.getType().isEmpty())
			throw new EmptyFieldsException("Item's type is empty");

		return true;
	}

	@Override
	public boolean isValidItemId(ItemIdBoundary iid) {
		if (iid == null)
			throw new EmptyFieldsException("An operation must be performed on a valid item");

		if (iid.getId() == null || iid.getSpace() == null || iid.getId().isEmpty() || iid.getSpace().isEmpty())
			throw new EmptyFieldsException("Invalid item's `Id` or `Space`");

		return true;
	}

	@Override
	public boolean isUserRole(UserEntity user, UserRole role) {
		return user.getRole() == role;
	}
	
	@Override
	public boolean isValidUseCase(String usecase) {
		try {
			UseCase.valueOf(usecase);
			return true;
		} catch (Exception e) {
			throw new EmptyFieldsException("Invalid usecase type");
		}
	}

	@Override
	public boolean isValidUserForUseCase(UserEntity user, UseCase usecase) {

		switch (usecase) {
		case FIX_VEHICLE:
			return this.isUserRole(user, UserRole.PLAYER);
		case MAINTENANCE_BY_MONTH:
			return this.isUserRole(user, UserRole.MANAGER) || this.isUserRole(user, UserRole.ADMIN);
		case GET_ALL_WORKERS:
			return this.isUserRole(user, UserRole.MANAGER) || this.isUserRole(user, UserRole.ADMIN);
		default:
			return false;
		}
	}

}
