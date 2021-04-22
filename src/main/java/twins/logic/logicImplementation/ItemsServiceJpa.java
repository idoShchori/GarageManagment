package twins.logic.logicImplementation;

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
import twins.items.ItemBoundary;
import twins.items.ItemId;
import twins.logic.ItemsService;

@Service
public class ItemsServiceJpa implements ItemsService {
	private ItemsDao itemsDao;
	private EntityConverter entityConverter;
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


	@Override
	@Transactional(readOnly = false) //The default value
	public ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) {
		ItemEntity entity = this.entityConverter.toEntity(item);
		entity.setItemId(new ItemIdPK(springApplicatioName,  UUID.randomUUID().toString()));
		this.itemsDao.save(entity);
		return this.entityConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemBoundary update) {
		
		Optional<ItemEntity> existingOptional = this.itemsDao.findById(userSpace + "/" + userEmail+"/"+itemSpace+"/"+itemId);
		if (existingOptional.isPresent()) {
			boolean dirty = false;

			ItemEntity existing =existingOptional.get();
			
			if (update.getType() != null) {
				existing.setType(update.getType());
				dirty = true;
			}

			if (update.getName() != null) {
				existing.setName(update.getName());
				dirty = true;
			}

			if (update.isActive() != null) {
				existing.setActive(update.isActive());
				dirty = true;
			}
			
		
			if (update.getCreatedBy() != null) {
				existing.setUserEmail(update.getCreatedBy().getUserId().getEmail());
				existing.setUserSpace(update.getCreatedBy().getUserId().getSpace());
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
			throw new RuntimeException("could not find item by userSpace/userEmail/itemSpace/itemId: " + userSpace + "/" + userEmail+"/"+itemSpace+"/"+itemId);// NullPointerException
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		//TODO: find specific user
		Iterable<ItemEntity>  allEntities = this.itemsDao.findAll();
		return StreamSupport
				.stream(allEntities.spliterator(), false)
				.filter(e-> e.getUserSpace().equals(userSpace) &&
						e.getUserEmail().equals(userEmail))
				.map(this.entityConverter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		Optional<ItemEntity> existingOptional = this.itemsDao.findById(userSpace + "/" + userEmail+"/"+itemSpace+"/"+itemId);
		if (existingOptional.isPresent()) {
			ItemEntity existing =existingOptional.get();
			ItemBoundary rv = this.entityConverter.toBoundary(existing);
			return rv;

		} else {
			// TODO have server return status 404 here
			throw new RuntimeException("could not find item by userSpace/userEmail/itemSpace/itemId: " + userSpace + "/" + userEmail+"/"+itemSpace+"/"+itemId);// NullPointerException
		}

	}

	@Override
	@Transactional(readOnly = false)//The default value
	public void deleteAllItems(String adminSpace, String adminEmail) {
		//TODO check if admin
		this.itemsDao.deleteAll();
	}

}
