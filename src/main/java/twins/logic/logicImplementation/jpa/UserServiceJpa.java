package twins.logic.logicImplementation.jpa;



import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import twins.data.UserEntity;
import twins.data.UserIdPK;
import twins.data.UserRole;
import twins.data.dao.UsersDao;
import twins.logic.UsersService;
import twins.logic.Exceptions.UserAccessDeniedException;
import twins.logic.Exceptions.UserNotFoundException;
import twins.logic.logicImplementation.EntityConverter;
import twins.users.UserBoundary;

@Service
public class UserServiceJpa implements UsersService{
	
	private UsersDao usersDao;
	private EntityConverter entityConverter;
	private String springApplicatioName;
	
	public UserServiceJpa() {
	}
	
	@Value("${spring.application.name:defaultName}")
	public void setSpringApplicatioName(String springApplicatioName) {
		this.springApplicatioName = springApplicatioName;
	}
	
	@Autowired
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}
	
	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}
	
	@PostConstruct
	public void init() {
		System.err.println("spring application name = " + this.springApplicatioName);
	}
	

	@Override
	@Transactional(readOnly = false) //The default value
	public UserBoundary createUser(UserBoundary user) {
		UserEntity entity = this.entityConverter.toEntity(user);
		entity.getUserId().setSpace(springApplicatioName);
		this.usersDao.save(entity);
		return this.entityConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userSpace, String userEmail) {
		//Users unique addressID combined from this String --> userSpace and userEmail (TOGETHER)
		
		Optional<UserEntity> optionalUser = this.usersDao.findById(new UserIdPK(userSpace,userEmail));
		if( optionalUser.isPresent()) {
			UserEntity entity = optionalUser.get();
			UserBoundary boundary = entityConverter.toBoundary (entity);
			return boundary;
		}
		else {
			// TODO have server return status 404 here
			throw new UserNotFoundException("Could not find user by userSpace/userEmail : " + userSpace+"/"+userEmail);// NullPointerException
		}
	}

	@Override
	@Transactional(readOnly = false) //The default value
	public UserBoundary updateUser(String userSpace, String userEmail, UserBoundary update) {
		// get existing user from database
		//Users unique addressID combined from this String --> userSpace and userEmail (TOGETHER)
		Optional<UserEntity> existingOptional = this.usersDao.findById(new UserIdPK(userSpace,userEmail));
		if( existingOptional.isPresent()) {
			UserEntity existing = existingOptional.get();
			// update collection and return update
			if (update.getUsername() != null) {
				existing.setUsername(update.getUsername());
			}
			
			if (update.getAvatar() != null) {
				existing.setAvatar(update.getAvatar());
			}
			
			if (update.getRole() != null) {
				existing.setRole(UserRole.valueOf(update.getRole()));
			}
			//userSpace and userEmail are never changed!!!!! (id)
			
			// update database
			existing = this.usersDao.save(existing);
			
			UserBoundary rv = this.entityConverter.toBoundary(existing);
			return rv;
		}else {
			throw new UserNotFoundException("could not find user by userSpace/userEmail: " + userSpace+"/"+userEmail);// NullPointerException
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail) {
		//Users unique addressID combined from this String --> userSpace and userEmail (TOGETHER)
		Optional<UserEntity> optionalUser = this.usersDao.findById(new UserIdPK(adminSpace,adminEmail));
		if( optionalUser.isPresent()) {
			UserEntity entity = optionalUser.get();
			if(entity.getRole() == UserRole.ADMIN) {
				Iterable<UserEntity>  allEntities = this.usersDao.findAll();
				return StreamSupport
						.stream(allEntities.spliterator(), false)
						.map(this.entityConverter::toBoundary)
						.collect(Collectors.toList());
			}else {
				throw new UserAccessDeniedException("User is not ADMIN,therefore access denied! ");//Not a Manager
			}
		}else {
			// TODO have server return status 404 here
			throw new UserNotFoundException("Could not find user by userSpace/userEmail : " + adminSpace+"/"+adminEmail);// NullPointerException
		}
	}

	@Override
	@Transactional(readOnly = false)//The default value
	public void deleteAllUsers(String adminSpace, String adminEmail) {
		//Users unique addressID combined from this String --> userSpace and userEmail (TOGETHER)
		Optional<UserEntity> optionalUser = this.usersDao.findById(new UserIdPK(adminSpace,adminEmail));
		if( optionalUser.isPresent()) {
			UserEntity entity = optionalUser.get();
			if(entity.getRole() == UserRole.ADMIN) {
				this.usersDao.deleteAll();
			}else {
				throw new UserAccessDeniedException("User is not ADMIN,therefore access denied! ");//Not a Manager
			}
		}else {
			// TODO have server return status 404 here
			throw new UserNotFoundException("Could not find user by userSpace/userEmail : " + adminSpace+"/"+adminEmail);// NullPointerException
		}
	}

}
