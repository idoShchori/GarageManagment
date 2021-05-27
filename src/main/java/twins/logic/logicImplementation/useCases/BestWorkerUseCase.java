package twins.logic.logicImplementation.useCases;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import twins.items.ItemBoundary;
import twins.items.ItemIdBoundary;
import twins.logic.Exceptions.IllegalDateException;
import twins.logic.Exceptions.IllegalItemTypeException;
import twins.operations.OperationBoundary;
import twins.users.UserBoundary;
import twins.users.UserId;

@Service
public class BestWorkerUseCase extends AbstractUseCase {

	public UserBoundary invoke(OperationBoundary operation) {
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
		int year = LocalDate.now().getYear();
		int month = LocalDate.now().getMonthValue();
		String startString = year + "-" + month + "-01";
		String endString = year + "-" + (month + 1) + "-01";
		if (month == 12)
			endString = (year + 1) + "-01-01";
		try {
			startDate =  new SimpleDateFormat("yyyy-MM-dd").parse(startString);
			endDate =  new SimpleDateFormat("yyyy-MM-dd").parse(endString);
		} catch (ParseException e) {
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
		
		return Collections.max(				//	extract the worker with the highest revenue
				workersRevenue.entrySet(),
				Comparator.comparingDouble(Map.Entry::getValue)).getKey();
	}
}
