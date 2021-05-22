package twins.logic.logicImplementation.useCases;

import twins.items.ItemBoundary;
import twins.items.ItemIdBoundary;
import twins.logic.Exceptions.IllegalItemTypeException;
import twins.operations.OperationBoundary;
import twins.users.UserId;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PendingMaintenanceList extends AbstractUseCase {
	
	public List<ItemBoundary> invoke(OperationBoundary operation, int size, int page) {
		
		UserId userId = operation.getInvokedBy().getUserId();

		ItemIdBoundary itemId = operation.getItem().getItemId();
		if (!this.itemsService.getSpecificItem(
								userId.getSpace(),
								userId.getEmail(),
								itemId.getSpace(),
								itemId.getId())
				.getType().equals("report")) {
			throw new IllegalItemTypeException("Item's type is not a report");
		}
		
		usersService.login(userId.getSpace(), userId.getEmail());
		
		return this.itemsService.getAllItemsByTypeAndActive("vehicle maintenance", true, size, page);

	}

}
