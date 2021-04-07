package twins.logic.logicImplementation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import twins.data.ItemEntity;
import twins.items.ItemBoundary;
import twins.logic.ItemsService;


@Service
public class ItemServiceMockup implements ItemsService{
	private Map<String,ItemEntity> items;
	private EntityConverter entityConverter;
	
	public ItemServiceMockup() {
		// create a thread safe collection
		this.items = Collections.synchronizedMap(new HashMap<>());
	}
	
	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}
	
	
	@Override
	public ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) {
		// MOCKUP
		ItemEntity entity = this.entityConverter.toEntity(item);

		this.items.put(userSpace + "/" + userEmail + "/" + item.getItemId().getSpace() + "/" + item.getItemId().getId(),
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
				existing.setCreatedBy(update.getCreatedBy());
				dirty = true;
			}
			
			if (update.getLocation() != null) {
				existing.setLocation(update.getLocation());
				dirty = true;
			}
			
			if (update.getItemAttributes() != null) {
				existing.setItemAttributes(update.getItemAttributes());
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

		return this.items
				.values()
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
		this.items.clear();
	}

}
