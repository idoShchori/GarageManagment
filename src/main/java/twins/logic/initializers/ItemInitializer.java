package twins.logic.initializers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import twins.data.UserRole;
import twins.items.ItemBoundary;
import twins.items.ItemIdBoundary;
import twins.items.Location;
import twins.logic.UpdatedItemsService;
import twins.logic.UpdatedUsersService;
import twins.users.User;
import twins.users.UserBoundary;
import twins.users.UserId;

@Component
@Profile("init_items")
public class ItemInitializer implements CommandLineRunner {

	private UpdatedItemsService itemsService;
	private UpdatedUsersService usersService;
	private Random rand = new Random();
	private String springApplicatioName;
	
	@Value("${spring.application.name:defaultName}")
	public void setSpringApplicatioName(String springApplicatioName) {
		this.springApplicatioName = springApplicatioName;
	}
	
	@Autowired
	public void setUsersService(UpdatedUsersService usersService) {
		this.usersService = usersService;
	}

	@Autowired
	public void setUsersService(UpdatedItemsService itemsService) {
		this.itemsService = itemsService;
	}
	
	private String generateLicenseNumber() {
		String num;
		
		int temp = rand.nextInt(999) + 1;
		while (temp < 100)
			temp *= 10;
		num = String.valueOf(temp) + "-";
		
		temp = rand.nextInt(99) + 1;
		while (temp < 10)
			temp *= 10;
		num += String.valueOf(temp) + "-";
		
		temp = rand.nextInt(999) + 1;
		while (temp < 100)
			temp *= 10;
		num += String.valueOf(temp);
		
		return num;
	}

	@Override
	public void run(String... args) throws Exception {

		int temp = rand.nextInt();
		String email = "managerTemp" + temp + "@email.com";
		UserBoundary user = new UserBoundary();
		user.setUsername("Example Manager");
		user.setAvatar("EP");
		user.setRole(UserRole.MANAGER.toString());
		UserId id = new UserId();
		id.setEmail(email);
		user.setUserId(id);
		this.usersService.createUser(user);

		User userToItem = new User();
		userToItem.setUserId(id);

		List<String> carTypes = new ArrayList<>();
		carTypes.add("Hyundai i10");
		carTypes.add("Hyundai i20");
		carTypes.add("Seat Ibiza");
		carTypes.add("Seat Leon");
		carTypes.add("Tesla Model 3");
		carTypes.add("Tesla Model S");
		carTypes.add("Tesla Model X");
		carTypes.add("BMW I8");
		carTypes.add("BMW X5");
		carTypes.add("Mercedes-Benz A35");
		carTypes.add("Mercedes-Benz G63");
		carTypes.add("Mercedes-Benz C43");

		IntStream.range(0, 20) // Stream<Integer>
				.mapToObj(i -> {

					ItemBoundary item = new ItemBoundary();

					item.setActive(true);
					item.setCreatedBy(userToItem);
					item.setLocation(new Location());
					item.setName("Vehicle " + carTypes.get(rand.nextInt(carTypes.size())));
					item.setType("vehicle");
					Map<String, Object> attr = new HashMap<>();
					attr.put("license number", generateLicenseNumber());
					item.setItemAttributes(attr);

					return item;
				}) // Stream<MessageBoundary>
				.forEach(item -> {
					ItemBoundary parent = this.itemsService.createItem(springApplicatioName, email, item);

					ItemBoundary child = new ItemBoundary();

					child.setActive(true);
					child.setCreatedBy(userToItem);
					child.setLocation(new Location());
					child.setName(parent.getName() + " maintenance");
					child.setType("vehicle maintenance");
					Map<String, Object> attr = new HashMap<>();
					attr.put("type", "fix");
					child.setItemAttributes(attr);

					child = this.itemsService.createItem(springApplicatioName, email, child);

					ItemIdBoundary iid = new ItemIdBoundary();
					iid.setSpace(springApplicatioName);
					iid.setId(child.getItemId().getId());

					this.itemsService.addChildToParent(springApplicatioName, email, springApplicatioName,
							parent.getItemId().getId(), iid);

				});
		
		ItemBoundary reportItem = new ItemBoundary();
		reportItem.setActive(true);
		reportItem.setCreatedBy(userToItem);
		reportItem.setLocation(new Location());
		reportItem.setName("Revenue report");
		reportItem.setType("report");
		this.itemsService.createItem(springApplicatioName, email, reportItem);
		
		
		reportItem = new ItemBoundary();
		reportItem.setActive(true);
		reportItem.setCreatedBy(userToItem);
		reportItem.setLocation(new Location());
		reportItem.setName("Best worker report");
		reportItem.setType("report");
		this.itemsService.createItem(springApplicatioName, email, reportItem);
		
		
		reportItem = new ItemBoundary();
		reportItem.setActive(true);
		reportItem.setCreatedBy(userToItem);
		reportItem.setLocation(new Location());
		reportItem.setName("All workers report");
		reportItem.setType("report");
		this.itemsService.createItem(springApplicatioName, email, reportItem);
		
		
		reportItem = new ItemBoundary();
		reportItem.setActive(true);
		reportItem.setCreatedBy(userToItem);
		reportItem.setLocation(new Location());
		reportItem.setName("Maintenance by date report");
		reportItem.setType("report");
		this.itemsService.createItem(springApplicatioName, email, reportItem);
		
		
		reportItem = new ItemBoundary();
		reportItem.setActive(true);
		reportItem.setCreatedBy(userToItem);
		reportItem.setLocation(new Location());
		reportItem.setName("Pending maintenances report");
		reportItem.setType("report");
		this.itemsService.createItem(springApplicatioName, email, reportItem);
		
		System.err.println("Initilizer profile is active - added 20 vehicles with maintenances and 5 types of reports");
	}

}
