package twins.logic.logicImplementation.useCases;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import twins.items.ItemBoundary;
import twins.items.Location;
import twins.logic.UpdatedItemsService;
import twins.logic.UsersService;
import twins.logic.Exceptions.EmptyFieldsException;
import twins.operations.OperationBoundary;
import twins.users.UserBoundary;
import twins.users.UserId;

@Service
public class FixVehicle {
	
	private UpdatedItemsService itemService;
	private UsersService usersService;
	
	@Autowired
	public void setItemService(UpdatedItemsService itemService) {
		this.itemService = itemService;
	}

	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	public void invoke(OperationBoundary operation) {
		UserId userId = operation.getInvokedBy().getUserId();
		UserBoundary user = usersService.login(userId.getSpace(), userId.getEmail());
		
		user.setRole("ADMIN");
		usersService.updateUser(userId.getSpace(), userId.getEmail(), user);
		
		ArrayList<String> allItems = (ArrayList<String>) operation.getOperationAttributes().get("items");
		if (allItems == null)
			throw new EmptyFieldsException("Array of items is required");
			
		for (String string : allItems) {
			ItemBoundary item = new ItemBoundary();
			item.setName(string);
			item.setType("Car item");
			item.setActive(true);
			item.setCreatedBy(operation.getInvokedBy());
			item.setLocation(new Location(0, 0));
			item.setActive(false);
			item = itemService.createItem(operation.getInvokedBy().getUserId().getSpace(),
					operation.getInvokedBy().getUserId().getEmail(), item);
			itemService.addChildToParent(
					operation.getInvokedBy().getUserId().getSpace(),
					operation.getInvokedBy().getUserId().getEmail(),
					operation.getItem().getItemId().getSpace(),
					operation.getItem().getItemId().getId(),
					item.getItemId());
		}
		
		ItemBoundary parent = itemService.getSpecificItem(
				operation.getInvokedBy().getUserId().getSpace(),
				operation.getInvokedBy().getUserId().getEmail(),
				operation.getItem().getItemId().getSpace(),
				operation.getItem().getItemId().getId());
		
		Map<String, Object> attributes = parent.getItemAttributes();
		attributes.put("isFixed", true);
		parent.setItemAttributes(attributes);
		parent.setActive(false);
		
		itemService.updateItem(
				operation.getInvokedBy().getUserId().getSpace(),
				operation.getInvokedBy().getUserId().getEmail(),
				operation.getItem().getItemId().getSpace(),
				operation.getItem().getItemId().getId(),
				parent);
		
		user.setRole("PLAYER");
		usersService.updateUser(userId.getSpace(), userId.getEmail(), user);
	}

}
