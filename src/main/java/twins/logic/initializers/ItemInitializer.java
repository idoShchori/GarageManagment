package twins.logic.initializers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import twins.data.dao.ItemsDao;
import twins.items.Item;
import twins.items.ItemBoundary;
import twins.items.ItemIdBoundary;
import twins.items.Location;
import twins.logic.UpdatedItemsService;
import twins.logic.UpdatedUsersService;
import twins.logic.logicImplementation.EntityConverter;
import twins.logic.logicImplementation.useCases.FixVehicleUseCase;
import twins.operations.OperationBoundary;
import twins.users.User;
import twins.users.UserBoundary;
import twins.users.UserId;

@Component
@Profile("init_items")
public class ItemInitializer implements CommandLineRunner {

	private UpdatedItemsService itemsService;
	private ItemsDao itemsDao;
	private UpdatedUsersService usersService;
	private EntityConverter entityConverter;
//	private FixVehicleUseCase fixVehicle;
	private Random rand = new Random();
	private String springApplicatioName;
	
	@Value("${spring.application.name:defaultName}")
	public void setSpringApplicatioName(String springApplicatioName) {
		this.springApplicatioName = springApplicatioName;
	}
	
	@Autowired
	public void setItemsDao(ItemsDao itemsDao) {
		this.itemsDao = itemsDao;
	}
	
	@Autowired
	public void setUsersService(UpdatedUsersService usersService) {
		this.usersService = usersService;
	}

	@Autowired
	public void setUsersService(UpdatedItemsService itemsService) {
		this.itemsService = itemsService;
	}
	
	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}
	
//	@Autowired
//	public void setFixVehicle(FixVehicleUseCase fixVehicle) {
//		this.fixVehicle = fixVehicle;
//	}
	
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
	
	private String getRandomDate() {
		String date = "";
		
		int year = rand.nextInt(32) + 1990;
		int month = rand.nextInt(12) + 1;
		int day = rand.nextInt(28) + 1;
		
		return year + "-" + month + "-" + day;
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
					
					try {
						Date parentDate = new SimpleDateFormat("yyyy-MM-dd").parse(getRandomDate());
						parent.setCreatedTimestamp(parentDate);
						
						Date childDate;
						do {
							childDate = new SimpleDateFormat("yyyy-MM-dd").parse(getRandomDate());
						} while (childDate.before(parentDate));
						
						child.setCreatedTimestamp(childDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					this.itemsDao.save(this.entityConverter.toEntity(parent));
					this.itemsDao.save(this.entityConverter.toEntity(child));

					this.itemsService.addChildToParent(springApplicatioName, email, springApplicatioName,
							parent.getItemId().getId(), iid);
					
//					
//					OperationBoundary op = new OperationBoundary();
//					op.setInvokedBy(userToItem);
//					ItemIdBoundary idB = new ItemIdBoundary(springApplicatioName, email);
//					op.setItem(new Item(idB));
//					Map<String, Object> m = new HashMap<>();
//					m.put("operationName", "FIX_VEHICLE");
//					m.put("items", new String[] {"door", "enegine"});
//					m.put("price", 400);
//					op.setOperationAttributes(m);
//					
//					this.fixVehicle.invoke(op);

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
