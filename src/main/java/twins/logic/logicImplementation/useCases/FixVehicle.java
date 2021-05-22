package twins.logic.logicImplementation.useCases;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

import twins.items.ItemBoundary;
import twins.items.Location;
import twins.logic.Exceptions.EmptyFieldsException;
import twins.operations.OperationBoundary;
import twins.users.UserBoundary;
import twins.users.UserId;

@Service
public class FixVehicle extends AbstractUseCase {

	public void invoke(OperationBoundary operation) {
		UserId userId = operation.getInvokedBy().getUserId();
		UserBoundary user = usersService.login(userId.getSpace(), userId.getEmail());
		
		user.setRole("MANAGER");
		usersService.updateUser(userId.getSpace(), userId.getEmail(), user);
		
		ArrayList<String> allItems = null;
		try {
			allItems = (ArrayList<String>) operation.getOperationAttributes().get("items");
			if (allItems == null)
				throw new EmptyFieldsException("Array of items is required");
		} catch (Exception e) {
			throw new EmptyFieldsException("Array of items is required");
		}
		
		Double price = null;
		try {
			price = Double.parseDouble(operation.getOperationAttributes().get("price").toString());
		} catch (Exception e) {
			throw new EmptyFieldsException("Price of maintenance is required");
		}
			
		for (String string : allItems) {
			ItemBoundary item = new ItemBoundary();
			item.setName(string);
			item.setType("car item");
			item.setActive(true);
			item.setCreatedBy(operation.getInvokedBy());
			item.setLocation(new Location(0, 0));
			item.setActive(false);
			item = itemsService.createItem(operation.getInvokedBy().getUserId().getSpace(),
					operation.getInvokedBy().getUserId().getEmail(), item);
			itemsService.addChildToParent(
					operation.getInvokedBy().getUserId().getSpace(),
					operation.getInvokedBy().getUserId().getEmail(),
					operation.getItem().getItemId().getSpace(),
					operation.getItem().getItemId().getId(),
					item.getItemId());
		}
		
		ItemBoundary parent = itemsService.getSpecificItem(
				operation.getInvokedBy().getUserId().getSpace(),
				operation.getInvokedBy().getUserId().getEmail(),
				operation.getItem().getItemId().getSpace(),
				operation.getItem().getItemId().getId());
		
		Map<String, Object> attributes = parent.getItemAttributes();
		attributes.put("isFixed", true);
		attributes.put("price", price);
		parent.setItemAttributes(attributes);
		parent.setActive(false);
		
		itemsService.updateItem(
				operation.getInvokedBy().getUserId().getSpace(),
				operation.getInvokedBy().getUserId().getEmail(),
				operation.getItem().getItemId().getSpace(),
				operation.getItem().getItemId().getId(),
				parent);
		
		user.setRole("PLAYER");
		usersService.updateUser(userId.getSpace(), userId.getEmail(), user);
	}

}
