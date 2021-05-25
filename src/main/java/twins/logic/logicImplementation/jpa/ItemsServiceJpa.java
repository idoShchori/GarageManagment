package twins.logic.logicImplementation.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import twins.data.ItemEntity;
import twins.data.ItemIdPK;
import twins.data.UserEntity;
import twins.data.UserIdPK;
import twins.data.UserRole;
import twins.data.dao.ItemsDao;
import twins.items.ItemBoundary;
import twins.items.ItemIdBoundary;
import twins.logic.UpdatedItemsService;
import twins.logic.UsersService;
import twins.logic.Exceptions.EmptyFieldsException;
import twins.logic.Exceptions.ItemNotFoundException;
import twins.logic.Exceptions.UserAccessDeniedException;
import twins.logic.logicImplementation.EntityConverter;
import twins.logic.logicImplementation.Validator;

@Service
public class ItemsServiceJpa implements UpdatedItemsService {
	private ItemsDao itemsDao;
	private UsersService usersService;
	private EntityConverter entityConverter;
	private Validator validator;
	private String springApplicatioName;

	@Value("${spring.application.name:defaultName}")
	public void setSpringApplicatioName(String springApplicatioName) {
		this.springApplicatioName = springApplicatioName;
	}

	@Autowired
	public void setItemsDao(ItemsDao itemsDao) {
		this.itemsDao = itemsDao;
	}

	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	
	@Override
	@Transactional(readOnly = false) // The default value
	public ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) {

		UserIdPK userId = new UserIdPK(userSpace, userEmail);
		//		if user does not exits, exception will be thrown inside this method
		UserEntity user = this.entityConverter.toEntity(this.usersService.login(userId.getSpace(), userId.getEmail()));
		if (validator.isUserRole(user, UserRole.PLAYER))
			throw new UserAccessDeniedException("User defined as `Player` can not perform this action");

		userId = user.getUserId();

		if (!validator.isValidEmail(userEmail))
			throw new EmptyFieldsException("User email is illegal");
		if (userSpace == null || userSpace.isEmpty())
			throw new EmptyFieldsException("User space must be specified");
		if (!validator.isValidItem(item))
			throw new EmptyFieldsException("Item has illegal attributes");

		ItemEntity entity = this.entityConverter.toEntity(item);
		entity.setUserEmail(userId.getEmail());
		entity.setUserSpace(userId.getSpace());
		entity.setCreatedTimestamp(new Date());
		entity.setItemIdPK(new ItemIdPK(springApplicatioName, UUID.randomUUID().toString()));
		this.itemsDao.save(entity);

		return this.entityConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemBoundary update) {

		UserIdPK userId = new UserIdPK(userSpace, userEmail);
		//		if user does not exits, exception will be thrown inside this method
		UserEntity user = this.entityConverter.toEntity(this.usersService.login(userId.getSpace(), userId.getEmail()));
		if (validator.isUserRole(user, UserRole.PLAYER))
			throw new UserAccessDeniedException("User defined as `Player` can not perform this action");

		ItemIdPK id = new ItemIdPK(itemSpace, itemId);
		Optional<ItemEntity> existingOptional = this.itemsDao.findById(id);
		if (existingOptional.isPresent()) {
			boolean dirty = false;

			ItemEntity existing = existingOptional.get();

			if (update.getType() != null && !update.getType().isEmpty()) {
				existing.setType(update.getType());
				dirty = true;
			}

			if (update.getName() != null && !update.getName().isEmpty()) {
				existing.setName(update.getName());
				dirty = true;
			}

			if (update.isActive() != null) {
				existing.setActive(update.isActive());
				dirty = true;
			}

			if (update.getLocation() != null) {
				existing.setLocationLat(update.getLocation().getLat());
				existing.setLocationLng(update.getLocation().getLng());
				dirty = true;
			}

			if (update.getItemAttributes() != null) {
				existing.setItemAttributes(this.entityConverter.fromMapToJson(update.getItemAttributes()));
				dirty = true;
			}

			// ItemId, CreatedTimestamp, CreatedBy are never changed!!!

			if (dirty) {// update database
				existing = this.itemsDao.save(existing);
			}

			ItemBoundary rv = this.entityConverter.toBoundary(existing);
			return rv;

		} else {
			throw new ItemNotFoundException("could not find item by userSpace/userEmail/itemSpace/itemId: " + userSpace + "/"
					+ userEmail + "/" + itemSpace + "/" + itemId);// NullPointerException
		}
	}

	@Override
	@Transactional(readOnly = true)
	@Deprecated
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		throw new RuntimeException("Deprecated method");
//		Iterable<ItemEntity> allEntities = this.itemsDao.findAll();
//		return StreamSupport.stream(allEntities.spliterator(), false)
////				.filter(e -> e.getUserSpace().equals(userSpace) && e.getUserEmail().equals(userEmail))
//				.map(this.entityConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail, int size, int page) {
		
		UserIdPK userId = new UserIdPK(userSpace, userEmail);
		//		if user does not exits, exception will be thrown inside this method
		UserEntity user = this.entityConverter.toEntity(this.usersService.login(userId.getSpace(), userId.getEmail()));
		
		//	if user defined as player, filter out all the non-active items
		if (validator.isUserRole(user, UserRole.PLAYER)) {
			return this.itemsDao.findAll(PageRequest.of(page, size, Direction.DESC, "createdTimestamp", "itemIdPK"))
					.getContent()
					.stream()
					.filter(item -> item.isActive().booleanValue())		//	filter out all items with active=false
					.map(this.entityConverter::toBoundary)
					.collect(Collectors.toList());
		}
		
		return this.itemsDao.findAll(PageRequest.of(page, size, Direction.DESC, "createdTimestamp", "itemIdPK"))
						.getContent()
						.stream()
						.map(this.entityConverter::toBoundary)
						.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		
		UserIdPK userId = new UserIdPK(userSpace, userEmail);
		//		if user does not exits, exception will be thrown inside this method
		UserEntity user = this.entityConverter.toEntity(this.usersService.login(userId.getSpace(), userId.getEmail()));
		
		ItemIdPK id = new ItemIdPK(itemSpace, itemId);
		Optional<ItemEntity> existingOptional = this.itemsDao.findById(id);
		if (existingOptional.isPresent()) {
			ItemEntity existing = existingOptional.get();
			//	non-active items do not exist for player users
			if (validator.isUserRole(user, UserRole.PLAYER) && !existing.isActive())
				throw new ItemNotFoundException("could not find item by userSpace/userEmail/itemSpace/itemId: " + userSpace + "/"
						+ userEmail + "/" + itemSpace + "/" + itemId);
			
			ItemBoundary rv = this.entityConverter.toBoundary(existing);
			return rv;

		} else {
			throw new ItemNotFoundException("could not find item by userSpace/userEmail/itemSpace/itemId: " + userSpace + "/"
					+ userEmail + "/" + itemSpace + "/" + itemId);
		}

	}

	@Override
	@Transactional(readOnly = false) // The default value
	public void deleteAllItems(String adminSpace, String adminEmail) {
		
		UserIdPK userId = new UserIdPK(adminSpace, adminEmail);
		//		if user does not exits, exception will be thrown inside this method
		UserEntity user = this.entityConverter.toEntity(this.usersService.login(userId.getSpace(), userId.getEmail()));
		
		if (!validator.isUserRole(user, UserRole.ADMIN))
			throw new UserAccessDeniedException("User's role is not admin");

		this.itemsDao.deleteAll();
	}

	@Override
	@Transactional(readOnly = false) // The default value
	public void addChildToParent(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemIdBoundary item) {

		UserIdPK userId = new UserIdPK(userSpace, userEmail);
		//		if user does not exits, exception will be thrown inside this method
		this.usersService.login(userId.getSpace(), userId.getEmail());

		ItemIdPK id = new ItemIdPK(itemSpace, itemId);
		ItemEntity parent = this.itemsDao.findById(id).orElseThrow(
				() -> new UserAccessDeniedException("could not find parent item by space:" + itemSpace + " id:" + itemId));
		
		ItemIdPK inputChildId = new ItemIdPK(item.getSpace(), item.getId());
		ItemEntity child = this.itemsDao.findById(inputChildId).orElseThrow(() -> new ItemNotFoundException(
				"could not find child item by space:" + inputChildId.getSpace() + " id:" + inputChildId.getId()));

		parent.addItem(child);

		this.itemsDao.save(parent);
		this.itemsDao.save(child);
	}

	@Override
	@Transactional(readOnly = true) // The default value
	@Deprecated
	public List<ItemBoundary> getAllChildren(String userSpace, String userEmail, String itemSpace, String itemId) {
		throw new RuntimeException("Deprecated method");
//
//		ItemIdPK id = new ItemIdPK(itemSpace, itemId);
//
//		ItemEntity parent = this.itemsDao.findById(id).orElseThrow(
//				() -> new ItemNotFoundException("could not find parent item by space:" + itemSpace + " id:" + itemId));
//
//		return parent.getChildren().stream().map(this.entityConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public List<ItemBoundary> getAllChildren(String userSpace, String userEmail, String itemSpace, String itemId,
			int size, int page) {

		UserIdPK userId = new UserIdPK(userSpace, userEmail);
		//		if user does not exits, exception will be thrown inside this method
		this.usersService.login(userId.getSpace(), userId.getEmail());

		ItemIdPK id = new ItemIdPK(itemSpace, itemId);
		System.out.println(itemSpace + " " + itemId);
		return this.itemsDao
				.findAllByParent_itemIdPK(id,
						PageRequest.of(page, size, Direction.DESC, "createdTimestamp", "itemIdPK"))
				.stream()
				.map(this.entityConverter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true) // The default value
	@Deprecated
	public List<ItemBoundary> getAllParents(String childSpace, String childId) {
		throw new RuntimeException("Deprecated method");

//		ItemIdPK id = new ItemIdPK(childSpace, childId);
//
//		ItemEntity child = this.itemsDao.findById(id).orElseThrow(() -> new ItemNotFoundException(
//				"could not find parent item by space:" + childSpace + " id:" + childId));
//
//		List<ItemBoundary> parents = new ArrayList<ItemBoundary>();
//		parents.add(entityConverter.toBoundary(child.getParent()));
//
//		return parents;
	}

	@Override
	public List<ItemBoundary> getAllParents(String userSpace, String userEmail, String childSpace, String childId,
			int size, int page) {

		UserIdPK userId = new UserIdPK(userSpace, userEmail);
		//		if user does not exits, exception will be thrown inside this method
		this.usersService.login(userId.getSpace(), userId.getEmail());

		ItemIdPK id = new ItemIdPK(childSpace, childId);
		
		Optional<ItemEntity> optionalChild = this.itemsDao.findById(id);
		if (!optionalChild.isPresent())
			throw new ItemNotFoundException("This item does not exist");
		
		List<ItemEntity> parents =  new ArrayList<ItemEntity>();
		parents.add(optionalChild.get().getParent());
		
		return parents
				.stream()
				.map(this.entityConverter::toBoundary)
				.collect(Collectors.toList());

	}

	@Override
	public List<ItemBoundary> getAllItemsByTypeAndDate(String userSpace, String userEmail, String type, Date date,
			int size, int page) {
		UserIdPK userId = new UserIdPK(userSpace, userEmail);
		
		//		if user does not exits, exception will be thrown inside this method
		this.usersService.login(userId.getSpace(), userId.getEmail());

		return this.itemsDao.findAllByTypeAndCreatedTimestamp(type, date,
										PageRequest.of(page, size, Direction.DESC, "createdTimestamp", "itemIdPK"))
							.stream()
							.map(this.entityConverter::toBoundary)
							.collect(Collectors.toList());
	}

	@Override
	public List<ItemBoundary> getAllItemsByTypeAndDateBetween(String type, Date startDate, Date endDate) {

		return this.itemsDao.findAllByTypeAndCreatedTimestampBetween(
								type,
								startDate,
								endDate)
							.stream()
							.map(this.entityConverter::toBoundary)
							.collect(Collectors.toList());
	}
	
	@Override
	public List<ItemBoundary> getAllItemsByDateBetween(Date startDate, Date endDate) {

		return this.itemsDao.findAllByCreatedTimestampBetween(
								startDate,
								endDate)
							.stream()
							.map(this.entityConverter::toBoundary)
							.collect(Collectors.toList());
	}
	
	@Override
	public List<ItemBoundary> getAllItemsByTypeAndActive(String type, boolean active, int size, int page) {
		
		return this.itemsDao.findAllByTypeAndActive(type, active,
										PageRequest.of(page, size, Direction.DESC, "createdTimestamp", "itemIdPK"))
							.stream()
							.map(this.entityConverter::toBoundary)
							.collect(Collectors.toList());
	}
}
