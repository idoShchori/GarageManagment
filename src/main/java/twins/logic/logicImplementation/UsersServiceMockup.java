package twins.logic.logicImplementation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;

import twins.logic.UsersService;
import twins.users.UserBoundary;
import twins.data.UserEntity;
import twins.data.UserRole;

//@Service
public class UsersServiceMockup implements UsersService {
	
	private Map<String, UserEntity> users;
	private EntityConverter entityConverter;
	private String springApplicatioName;
	
	public UsersServiceMockup() {
		// create a thread safe collection
		this.users = Collections.synchronizedMap(new HashMap<>());
	}
	
	@Value("${spring.application.name:defaultName}")
	public void setSpringApplicatioName(String springApplicatioName) {
		this.springApplicatioName = springApplicatioName;
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
	public UserBoundary createUser(UserBoundary user) {
		//MOCKUP
		UserEntity entity = this.entityConverter.toEntity(user);
		entity.getUserId().setSpace(springApplicatioName);
		
		this.users.put(user.getUserId().getSpace()+"/"+user.getUserId().getEmail(), entity);
		
		return this.entityConverter.toBoundary(entity);
	}

	@Override
	public UserBoundary login(String userSpace, String userEmail) {
		// MOCKUP
		UserEntity entity = this.users.get(userSpace+"/"+userEmail);//Making sure that users from other spaces can login into system
		if (entity != null) {
			UserBoundary boundary = entityConverter.toBoundary (entity);
			return boundary;
		}else {
			// TODO have server return status 404 here
			throw new RuntimeException("Could not find user by userSpace/userEmail : " + userSpace+"/"+userEmail);// NullPointerException
		}
	}

	@Override
	public UserBoundary updateUser(String userSpace, String userEmail, UserBoundary update) {
		// get existing user from mockup database
		UserEntity existing = this.users.get(userSpace+"/"+userEmail);
		if (existing != null) {
			boolean dirty = false;
			
			// update collection and return update
			if (update.getUsername() != null) {
				existing.setUsername(update.getUsername());
				dirty = true;
			}
			
			if (update.getAvatar() != null) {
				existing.setAvatar(update.getAvatar());
				dirty = true;
			}
			
			if (update.getRole() != null) {
				existing.setRole(UserRole.valueOf(update.getRole()));
				dirty = true;
			}
			
			//userSpace and userEmail are never changed!!!!! (id)
		
			// update mockup database
			if (dirty) {
				this.users.put(userSpace+"/"+userEmail, existing);
			}
			
			UserBoundary rv = this.entityConverter.toBoundary(existing);
			return rv;
			
		}else {
			// TODO have server return status 404 here
			throw new RuntimeException("could not find user by userSpace/userEmail: " + userSpace+"/"+userEmail);// NullPointerException
		}
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail) {
		// MOCKUP
		UserEntity entity = this.users.get(adminSpace+"/"+adminEmail);
		if (entity != null) {
			if(entity.getRole() == UserRole.ADMIN) {
				return this.users
						.values()
						.stream()
						.map(this.entityConverter::toBoundary)
						.collect(Collectors.toList());
			}
			else {
				throw new RuntimeException("User is not ADMIN,therefore access denied! ");//Not a Manager
			}
		}else {
			// TODO have server return status 404 here
			throw new RuntimeException("Could not find user by userSpace/userEmail : " + adminSpace+"/"+adminEmail);// NullPointerException
		}
		
	}

	@Override
	public void deleteAllUsers(String adminSpace, String adminEmail) {
		// MOCKUP
		UserEntity entity = this.users.get(adminSpace+"/"+adminEmail);
		if (entity != null) {
			if(entity.getRole() == UserRole.ADMIN) {
				this.users.clear();
			}
			else {
				throw new RuntimeException("User is not ADMIN,therefore access denied! ");//Not a Manager
			}
		}else {
			// TODO have server return status 404 here
			throw new RuntimeException("Could not find user by userSpace/userEmail : " + adminSpace+"/"+adminEmail);// NullPointerException
		}
		
	}

}
