package twins.logic.logicImplementation.useCases;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import twins.items.ItemBoundary;
import twins.items.ItemIdBoundary;
import twins.logic.Exceptions.IllegalDateException;
import twins.logic.Exceptions.IllegalItemTypeException;
import twins.operations.OperationBoundary;
import twins.users.UserBoundary;
import twins.users.UserId;

@Service
public class GetRevenueReport extends AbstractUseCase{
	
	public double invoke(OperationBoundary operation) {
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
		
		String maintenanceType = "car maintenance";
		
//		SELECT * WHERE TYPE='something' AND DATE BETWEEN (start_date AND end_date)
		List<ItemBoundary> items = this.itemsService
										.getAllItemsByTypeAndDateBetween(
												maintenanceType,
												startDate,
												endDate);
		
		System.err.println(items);
		
		double totalPrice = 0;
		
		for (ItemBoundary item : items) {
			try {
				double thisPrice = Double.parseDouble(item.getItemAttributes().get("price").toString());
				totalPrice += thisPrice;
			} catch (Exception e) {
				// do nothing -> this car maintenance does not have a price in it's attributes, continue to others
			}
		}
		
		user.setRole("PLAYER");
		usersService.updateUser(userId.getSpace(), userId.getEmail(), user);
		
		return totalPrice;
	}
}
