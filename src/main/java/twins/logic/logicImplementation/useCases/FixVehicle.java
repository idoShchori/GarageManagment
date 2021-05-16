package twins.logic.logicImplementation.useCases;

import java.rmi.activation.ActivateFailedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import twins.data.OperationEntity;
import twins.data.UserEntity;
import twins.data.UserIdPK;
import twins.data.UserRole;
import twins.data.dao.ItemsDao;
import twins.data.dao.UsersDao;
import twins.items.ItemBoundary;
import twins.items.Location;
import twins.logic.ItemsRelationshipService;
import twins.logic.ItemsService;
import twins.logic.UsersService;
import twins.logic.Exceptions.EmptyFieldsException;
import twins.logic.Exceptions.UserNotFoundException;
import twins.logic.logicImplementation.EntityConverter;
import twins.operations.OperationBoundary;
import twins.users.User;
import twins.users.UserBoundary;
import twins.users.UserId;

@Service
public class FixVehicle {
	private ItemsRelationshipService itemService;
	private UsersService usersService;
	private UsersDao usersDao;
	private EntityConverter entityConverter;

	@Autowired
	public void setItemService(ItemsRelationshipService itemService) {
		this.itemService = itemService;
	}

	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	
	@Autowired
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}
	
	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}

	public void invoke(OperationBoundary operation) {
		UserId temp = operation.getInvokedBy().getUserId();
		UserIdPK id = new UserIdPK(temp.getSpace(), temp.getEmail());
		Optional<UserEntity> optionalUser = this.usersDao.findById(id);
		if (!optionalUser.isPresent())
			throw new UserNotFoundException("User not found");
		
		UserBoundary user = this.entityConverter.toBoundary(optionalUser.get());
		user.setRole("ADMIN");
		usersService.updateUser(id.getSpace(), id.getEmail(), user);
		
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
			item = itemService.createItem(operation.getInvokedBy().getUserId().getSpace(),
					operation.getInvokedBy().getUserId().getEmail(), item);
			System.out.println(item);
			itemService.addChildToParent(operation.getInvokedBy().getUserId().getSpace(),
					operation.getInvokedBy().getUserId().getEmail(), operation.getItem().getItemId().getSpace(),
					operation.getItem().getItemId().getId(), item.getItemId());
		}
		
		user.setRole("PLAYER");
		usersService.updateUser(id.getSpace(), id.getEmail(), user);
	}

}
