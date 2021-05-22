package twins.logic.logicImplementation.useCases;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

import twins.items.ItemBoundary;
import twins.items.Location;
import twins.logic.Exceptions.EmptyFieldsException;
import twins.logic.Exceptions.IllegalItemTypeException;
import twins.operations.OperationBoundary;
import twins.users.UserBoundary;
import twins.users.UserId;

@Service
public class FixVehicle extends AbstractUseCase {

	public void invoke(OperationBoundary operation) {
		UserId userId = operation.getInvokedBy().getUserId();
		UserBoundary user = usersService.login(userId.getSpace(), userId.getEmail());
		
		ItemBoundary item = itemsService.getSpecificItem(
				userId.getSpace(),
				userId.getEmail(),
				operation.getItem().getItemId().getSpace(),
				operation.getItem().getItemId().getId());
		
		// validate type of item is `vehicle maintenance`
		if(!item.getType().equals("vehicle maintenance"))
			throw new IllegalItemTypeException("This item's type is " + item.getType() + ", and not vehicle maintenance");
		
		// validate the type of this vehicle maintenance item is exists and is `fix`
		if (item.getItemAttributes().get("type") == null || !item.getItemAttributes().get("type").equals("fix"))
			throw new IllegalItemTypeException("This maintenance's type is not `fix` or not specified");
		
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
		
		user.setRole("MANAGER");
		usersService.updateUser(userId.getSpace(), userId.getEmail(), user);
			
		for (String string : allItems) {
			ItemBoundary carItem = new ItemBoundary();
			carItem.setName(string);
			carItem.setType("car item");
			carItem.setActive(true);
			carItem.setCreatedBy(operation.getInvokedBy());
			carItem.setLocation(new Location(0, 0));
			carItem.setActive(false);
			carItem = itemsService.createItem(operation.getInvokedBy().getUserId().getSpace(),
					operation.getInvokedBy().getUserId().getEmail(), carItem);
			itemsService.addChildToParent(
					operation.getInvokedBy().getUserId().getSpace(),
					operation.getInvokedBy().getUserId().getEmail(),
					operation.getItem().getItemId().getSpace(),
					operation.getItem().getItemId().getId(),
					carItem.getItemId());
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
