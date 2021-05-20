package twins.data.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import twins.data.UserEntity;
import twins.data.UserIdPK;
import twins.data.UserRole;

public interface UsersDao extends PagingAndSortingRepository<UserEntity, UserIdPK> {

	public List<UserEntity> findAllByRole(
			@Param("role") UserRole role,
			Pageable pageable);

}