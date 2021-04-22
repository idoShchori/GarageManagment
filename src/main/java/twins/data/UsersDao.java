package twins.data;

import org.springframework.data.repository.CrudRepository;

public interface UsersDao extends CrudRepository<UserEntity, UserIdPK>{
	

}