package twins.logic.logicImplementation;

import org.springframework.data.repository.CrudRepository;

import twins.data.UserEntity;
import twins.data.UserIdPK;

public interface UsersDao extends CrudRepository<UserEntity, UserIdPK>{
	

}