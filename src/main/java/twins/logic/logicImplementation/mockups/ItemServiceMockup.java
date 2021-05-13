package twins.logic.logicImplementation.mockups;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import twins.data.ItemEntity;
import twins.data.ItemIdPK;
import twins.items.ItemBoundary;
import twins.logic.ItemsService;
import twins.logic.logicImplementation.EntityConverter;


//@Service
public class ItemServiceMockup implements ItemsService{
	
	private Map<String,ItemEntity> items;
	private EntityConverter entityConverter;
	private String springApplicatioName;

	
	public ItemServiceMockup() {
		// create a thread safe collection
		this.items = Collections.synchronizedMap(new HashMap<>());
	}
	
	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}
	
	@Value("${spring.application.name:defaultName}")
	public void setSpringApplicatioName(String springApplicatioName) {
		this.springApplicatioName = springApplicatioName;
	}

	
	@Override
	public ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) {
		// MOCKUP
		ItemEntity entity = this.entityConverter.toEntity(item);
		
		entity.setItemId(new ItemIdPK(springApplicatioName,  UUID.randomUUID().toString()));
	
		this.items.put(userSpace + "/" + userEmail + "/" + springApplicatioName + "/" +entity.getItemId().getId(),
				entity);

		return this.entityConverter.toBoundary(entity);
	}

	@Override
	public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemBoundary update) {
		// get existing user from mockup database
		ItemEntity existing = this.items.get(userSpace + "/" + userEmail+"/"+itemSpace+"/"+itemId);

		if (existing != null) {
			boolean dirty = false;

			
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
			
			// update mockup database
			if (dirty) {
				this.items.put(userSpace + "/" + userEmail+"/"+itemSpace+"/"+itemId, existing);
			}

			ItemBoundary rv = this.entityConverter.toBoundary(existing);
			return rv;

		} else {
			// TODO have server return status 404 here
			throw new RuntimeException("could not find item by userSpace/userEmail/itemSpace/itemId: " + userSpace + "/" + userEmail+"/"+itemSpace+"/"+itemId);// NullPointerException
		}

	}

	@Override
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		//TODO: find specific user
		return this.items.values()
				.stream()
				.map(this.entityConverter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		
		ItemEntity entity = this.items.get(userSpace + "/" + userEmail+"/"+itemSpace+"/"+itemId);
		if(entity != null) {
			ItemBoundary boundary = entityConverter.toBoundary(entity);
			return boundary;
		}else
			throw new RuntimeException("could not find item by userSpace/userEmail: " + userSpace + "/" + userEmail);// NullPointerException
	}

	@Override
	public void deleteAllItems(String adminSpace, String adminEmail) {
		//TODO: check if user is admin
		this.items.clear();
	}

	@Override
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

}
