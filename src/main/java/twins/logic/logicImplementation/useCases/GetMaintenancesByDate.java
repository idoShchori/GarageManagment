package twins.logic.logicImplementation.useCases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import twins.data.ItemEntity;
import twins.data.UserEntity;
import twins.data.UserIdPK;
import twins.data.dao.ItemsDao;
import twins.data.dao.UsersDao;
import twins.logic.ItemsRelationshipService;
import twins.logic.UsersService;
import twins.logic.Exceptions.UserNotFoundException;
import twins.logic.logicImplementation.EntityConverter;
import twins.operations.OperationBoundary;
import twins.users.UserBoundary;
import twins.users.UserId;

public class GetMaintenancesByDate {
	private ItemsRelationshipService itemService;
	private UsersService usersService;
	private UsersDao usersDao;
	private EntityConverter entityConverter;
	private ItemsDao itemsDao;

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
	
	@Autowired
	public void setItemsDao(ItemsDao itemsDao) {
		this.itemsDao = itemsDao;
	}


	public List<ItemEntity> invoke(OperationBoundary operation,int page,int size) {
		UserId temp = operation.getInvokedBy().getUserId();
		UserIdPK id = new UserIdPK(temp.getSpace(), temp.getEmail());
		Optional<UserEntity> optionalUser = this.usersDao.findById(id);
		if (!optionalUser.isPresent())
			throw new UserNotFoundException("User not found");
		
		UserBoundary user = this.entityConverter.toBoundary(optionalUser.get());
		user.setRole("ADMIN");
		usersService.updateUser(id.getSpace(), id.getEmail(), user);
		
		Date date = (Date) operation.getOperationAttributes().get("Date");
		return itemsDao.findAllByTypeAndCreatedTimestamp("Car item",date,
				PageRequest.of(page, size, Direction.DESC, "createdTimestamp", "itemIdPK"));
		
	}
}
