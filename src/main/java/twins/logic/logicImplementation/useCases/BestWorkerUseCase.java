package twins.logic.logicImplementation.useCases;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import twins.items.ItemBoundary;
import twins.items.ItemIdBoundary;
import twins.logic.Exceptions.EmptyFieldsException;
import twins.logic.Exceptions.IllegalDateException;
import twins.logic.Exceptions.IllegalItemTypeException;
import twins.logic.Exceptions.NoMaintenancesInDate;
import twins.operations.OperationBoundary;
import twins.users.UserBoundary;
import twins.users.UserId;

@Service
public class BestWorkerUseCase extends AbstractUseCase {

	public Map<String, Object> invoke(OperationBoundary operation) {
		UserId userId = operation.getInvokedBy().getUserId();
		UserBoundary user = usersService.login(userId.getSpace(), userId.getEmail());
		
		// change user's role to MANAGER to get all the non-active items
		user.setRole("MANAGER");
		usersService.updateUser(userId.getSpace(), userId.getEmail(), user);
		
		ItemIdBoundary itemId = operation.getItem().getItemId();
		if (!this.itemsService.getSpecificItem(userId.getSpace(), userId.getEmail(), itemId.getSpace(), itemId.getId())
				.getType().equals("report")) {
			throw new IllegalItemTypeException("Item's type is not a report");
		}
		
		Date startDate, endDate;
		int month, year;
		String stringStartDate, stringEndDate;
		
		if (!operation.getOperationAttributes().containsKey("year") || !operation.getOperationAttributes().containsKey("month")) {
			throw new EmptyFieldsException("Year and month must be specified");
		}
				
		try {
			
			month = Integer.parseInt(operation.getOperationAttributes().get("month").toString());
			year = Integer.parseInt(operation.getOperationAttributes().get("year").toString());
			
			stringStartDate = year + "-" + month + "-01";
			stringEndDate = year + "-" + (month + 1) + "-01";
			
			if (month == 12)
				stringEndDate = (year + 1) + "-01-01";
			
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringStartDate);
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringEndDate);
		} catch (Exception e) {
			throw new IllegalDateException("Illegal month or year");
		}
		
		List<ItemBoundary> items = this.itemsService
				.getAllItemsByTypeAndDateBetween(
						"vehicle maintenance",
						startDate,
						endDate);
		
		Map<UserBoundary, Double> workersRevenue = new HashMap<>();
		
		items.forEach(item -> {
			if (item.getItemAttributes().containsKey("workerSpace") && item.getItemAttributes().containsKey("workerEmail")) {
								
				String space = item.getItemAttributes().get("workerSpace").toString();
				String email = item.getItemAttributes().get("workerEmail").toString();

				UserBoundary tempUser = usersService.login(space, email);
				if (item.getItemAttributes().containsKey("price")) {
					double total = Double.parseDouble(item.getItemAttributes().get("price").toString());
					if (workersRevenue.containsKey(tempUser)) {
						total += workersRevenue.get(tempUser);
					}
					workersRevenue.put(tempUser, total);
				}
			}
		});
		
		user.setRole("PLAYER");
		usersService.updateUser(userId.getSpace(), userId.getEmail(), user);
		
		if (workersRevenue.isEmpty())
			throw new NoMaintenancesInDate("There are no maintenances in the specified date");
		
		Map.Entry<UserBoundary, Double> theBestWorker = Collections.max(				//	extract the worker with the highest revenue
																	workersRevenue.entrySet(),
																	Comparator.comparingDouble(Map.Entry::getValue));
		
		Map<String, Object> returnedValues = new HashMap<String, Object>();
		returnedValues.put("user", theBestWorker.getKey());
		returnedValues.put("revenue", theBestWorker.getValue());
		
		return returnedValues;
	}
}
