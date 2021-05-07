package twins.logic.logicImplementation.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import twins.data.ItemEntity;
import twins.data.ItemIdPK;
import twins.data.dao.ItemsDao;
import twins.items.ItemBoundary;
import twins.items.ItemIdBoundary;
import twins.logic.UpdatedItemsService;
import twins.logic.Exceptions.EmptyFieldsException;
import twins.logic.Exceptions.ItemNotFoundException;
import twins.logic.logicImplementation.EntityConverter;

@Service
public class ItemsServiceJpa implements UpdatedItemsService {
	private ItemsDao itemsDao;
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

		// TODO: extract the user details from database, and create a new UserEntity,
		// and set item's
		// createdBy attribute to be that UserEntity

		if (validator.isValidItem(item) && validator.isValidEmail(userEmail) && userSpace != null && !userSpace.isEmpty()) {

			ItemEntity entity = this.entityConverter.toEntity(item);
			entity.setUserEmail(userEmail);
			entity.setUserSpace(userSpace);
			entity.setCreatedTimestamp(new Date());
			entity.setItemId(new ItemIdPK(springApplicatioName, UUID.randomUUID().toString()));
			this.itemsDao.save(entity);
			return this.entityConverter.toBoundary(entity);

		} else {
			throw new EmptyFieldsException("could not create item -eror in one or more fileds");// NullPointerException
		}
	}

	@Override
	@Transactional(readOnly = false)
	public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemBoundary update) {
		// TODO: check if user exist

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
			// TODO have server return status 404 here
			throw new RuntimeException("could not find item by userSpace/userEmail/itemSpace/itemId: " + userSpace + "/"
					+ userEmail + "/" + itemSpace + "/" + itemId);// NullPointerException
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		// TODO: find specific user

		Iterable<ItemEntity> allEntities = this.itemsDao.findAll();
		return StreamSupport.stream(allEntities.spliterator(), false)
//				.filter(e -> e.getUserSpace().equals(userSpace) && e.getUserEmail().equals(userEmail))
				.map(this.entityConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		// TODO: check if user exist

		ItemIdPK id = new ItemIdPK(itemSpace, itemId);

		Optional<ItemEntity> existingOptional = this.itemsDao.findById(id);
		if (existingOptional.isPresent()) {
			ItemEntity existing = existingOptional.get();
			ItemBoundary rv = this.entityConverter.toBoundary(existing);
			return rv;

		} else {
			// TODO have server return status 404 here
			throw new RuntimeException("could not find item by userSpace/userEmail/itemSpace/itemId: " + userSpace + "/"
					+ userEmail + "/" + itemSpace + "/" + itemId);// NullPointerException
		}

	}

	@Override
	@Transactional(readOnly = false) // The default value
	public void deleteAllItems(String adminSpace, String adminEmail) {
		// TODO: check if admin
		this.itemsDao.deleteAll();
	}

	@Override
	@Transactional(readOnly = false) // The default value
	public void addChildToParent(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemIdBoundary item) {
		// TODO: check if user exist

		ItemIdPK id = new ItemIdPK(itemSpace, itemId);

		ItemEntity parent = this.itemsDao.findById(id).orElseThrow(
				() -> new ItemNotFoundException("could not find parent item by space:" + itemSpace + " id:" + itemId));

		ItemIdPK inputChildId = new ItemIdPK(item.getSpace(), item.getId());

		ItemEntity child = this.itemsDao.findById(inputChildId).orElseThrow(() -> new ItemNotFoundException(
				"could not find child item by space:" + inputChildId.getSpace() + " id:" + inputChildId.getId()));

		parent.addItem(child);

		this.itemsDao.save(parent);
		this.itemsDao.save(child);
	}

	@Override
	@Transactional(readOnly = true) // The default value
	public List<ItemBoundary> getAllChildren(String userSpace, String userEmail, String itemSpace, String itemId) {
		// TODO: check if user exist

		ItemIdPK id = new ItemIdPK(itemSpace, itemId);

		ItemEntity parent = this.itemsDao.findById(id).orElseThrow(
				() -> new ItemNotFoundException("could not find parent item by space:" + itemSpace + " id:" + itemId));

		return parent.getChildren().stream().map(this.entityConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true) // The default value
	public List<ItemBoundary> getAllParents(String childSpace, String childId) {

		ItemIdPK id = new ItemIdPK(childSpace, childId);

		ItemEntity child = this.itemsDao.findById(id).orElseThrow(() -> new ItemNotFoundException(
				"could not find parent item by space:" + childSpace + " id:" + childId));

		List<ItemBoundary> parents = new ArrayList<ItemBoundary>();
		parents.add(entityConverter.toBoundary(child.getParent()));

		return parents;

//		TODO: implementation of manyToMany relationship
//		if (child.getParent() != null) {
//			return Optional.of(
//					child.getParent()
//					.stream()
//					.map(this.entityConverter::toBoundary)
//					.collect(Collectors.toList()));
//		} else {
//			return Optional.empty();
//		}
	}
}
