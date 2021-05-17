package twins.data.dao;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import twins.data.UserEntity;
import twins.data.UserIdPK;

public interface UsersDao extends PagingAndSortingRepository<UserEntity, UserIdPK>{
//CrudRepository<UserEntity, UserIdPK>{
	

}