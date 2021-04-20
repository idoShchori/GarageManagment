package twins.logic.logicImplementation;

import org.springframework.data.repository.CrudRepository;

import twins.data.UserEntity;

public interface UsersDao extends CrudRepository<UserEntity, String>{
	

}