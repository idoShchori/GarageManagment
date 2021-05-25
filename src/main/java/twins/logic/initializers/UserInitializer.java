package twins.logic.initializers;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import twins.data.UserRole;
import twins.logic.UpdatedUsersService;
import twins.users.UserBoundary;
import twins.users.UserId;

@Component
@Profile("init_users")
public class UserInitializer implements CommandLineRunner{
	
	private UpdatedUsersService usersService;
	private Random rand = new Random();
	
	@Autowired
	public void setUsersService(UpdatedUsersService usersService) {
		this.usersService = usersService;
	}
	
	private String generateAvatar() {
		String avatar = "";
		avatar += (char)(rand.nextInt(25) + 65);
		avatar += (char)(rand.nextInt(25) + 65);
		
		return avatar;
	}

	@Override
	public void run(String... args) throws Exception {
		
		Arrays.asList(UserRole.values()).forEach( type -> {
			IntStream.range(0, 20) // Stream<Integer>
			.mapToObj( i -> {
				UserBoundary user = new UserBoundary();
				user.setUsername("Example " + type.toString() + i);
				user.setAvatar(generateAvatar());
				user.setRole(type.toString());
				UserId id = new UserId();
				id.setEmail("temp" + i + type + "@email.com");
				user.setUserId(id);
				
				return user;
			}) // Stream<MessageBoundary>
			.forEach(this.usersService::createUser);
		});
		
		System.err.println("Initilizer profile is active - added 20 examples for each user type to the database");
	}

}
