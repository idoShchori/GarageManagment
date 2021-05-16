package twins.logic.logicImplementation.useCases;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import twins.data.OperationEntity;
import twins.data.UserEntity;
import twins.data.UserRole;
import twins.data.dao.ItemsDao;
import twins.data.dao.UsersDao;
import twins.items.ItemBoundary;
import twins.items.Location;
import twins.logic.ItemsRelationshipService;
import twins.logic.ItemsService;
import twins.logic.logicImplementation.EntityConverter;
import twins.operations.OperationBoundary;
@Service
public class FixVehicle {
	private UsersDao usersDao;
	private ItemsRelationshipService itemService;
	
	@Autowired
	public void setItemService(ItemsRelationshipService itemService) {
		this.itemService=itemService;
	}
	
	@Autowired
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}
	
	public void invoke(OperationBoundary operation){
		ArrayList<String> allItems=(ArrayList<String>) operation.getOperationAttributes().get("items");
		for (String string : allItems) {
			ItemBoundary item = new ItemBoundary();
			item.setName(string);
			item.setType("Car item");
			item.setActive(true);
			item.setCreatedBy(operation.getInvokedBy());
			item.setLocation(new Location(0,0));
			item=itemService.createItem(operation.getInvokedBy().getUserId().getSpace(),
					operation.getInvokedBy().getUserId().getEmail(), item);
			itemService.addChildToParent(operation.getInvokedBy().getUserId().getSpace(),
					operation.getInvokedBy().getUserId().getEmail(),
					operation.getItem().getItemId().getSpace(),
					operation.getItem().getItemId().getId(), item.getItemId());
		}
	}

}


