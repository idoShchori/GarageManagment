package twins.logic;

import java.util.List;

import twins.data.UserRole;
import twins.users.UserBoundary;

public interface UpdatedUsersService extends UsersService {

	public List<UserBoundary> getAllUsersByRole(UserRole role, int size, int page);

}
