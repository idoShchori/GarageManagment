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
public class GetMaintenancesByDateUseCase extends AbstractUseCase {

	public List<ItemBoundary> invoke(OperationBoundary operation, int size, int page) {
		UserId userId = operation.getInvokedBy().getUserId();
		UserBoundary user = usersService.login(userId.getSpace(), userId.getEmail());

		// change user's role to MANAGER to get all the non-active items
		user.setRole("MANAGER");
		usersService.updateUser(userId.getSpace(), userId.getEmail(), user);

		String stringDate = operation.getOperationAttributes().get("date").toString();
		Date date;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
		} catch (Exception e) {
			throw new IllegalDateException("Illegal date. Date format is yyyy-MM-dd");
		}

		ItemIdBoundary itemId = operation.getItem().getItemId();
		if (!this.itemsService.getSpecificItem(userId.getSpace(), userId.getEmail(), itemId.getSpace(), itemId.getId())
				.getType().equals("report")) {
			throw new IllegalItemTypeException("Item's type is not a report");
		}

		String vehicleType = "vehicle maintenance";

		List<ItemBoundary> cars = this.itemsService
				.getAllItemsByTypeAndDate(userId.getSpace(), userId.getEmail(),
				vehicleType, date, size, page);

		user.setRole("PLAYER");
		usersService.updateUser(userId.getSpace(), userId.getEmail(), user);

		return cars;
	}
}
