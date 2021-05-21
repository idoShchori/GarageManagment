package twins.logic.logicImplementation;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import twins.data.ItemEntity;
import twins.data.ItemIdPK;
import twins.data.OperationEntity;
import twins.data.OperationIdPK;
import twins.data.UserEntity;
import twins.data.UserIdPK;
import twins.data.UserRole;
import twins.items.Item;
import twins.items.ItemBoundary;
import twins.items.ItemIdBoundary;
import twins.items.Location;
import twins.operations.OperationBoundary;
import twins.operations.OperationId;
import twins.users.User;
import twins.users.UserBoundary;
import twins.users.UserId;

@Component
public class EntityConverterImplementation implements EntityConverter {
	private ObjectMapper jackson;
	
	public EntityConverterImplementation() {
		this.jackson = new ObjectMapper();
	}
	
	@Override
	public OperationBoundary toBoundary(OperationEntity input) {
		OperationBoundary rv = new OperationBoundary();
		
		OperationIdPK idPK = input.getOperationIdPK();

		OperationId opId = new OperationId(idPK.getOperationSpace(), idPK.getOperationId());
		rv.setOperationId(opId);

		ItemIdBoundary itId = new ItemIdBoundary(input.getItemSpace(), input.getItemId());
		rv.setItem(new Item(itId));

		UserId usId = new UserId(input.getUserSpace(), input.getUserEmail());
		rv.setInvokedBy(new User(usId));

		rv.setType(input.getType());

		rv.setCreatedTimestamp(input.getCreatedTimestamp());

		rv.setOperationAttributes(this.fromJsonToMap(input.getOperationAttributes()));

		return rv;
	}

	@Override
	public OperationEntity toEntity(OperationBoundary input) {
		OperationEntity rv = new OperationEntity();

		if (input.getOperationId() != null) {
			OperationId opId = input.getOperationId();
			OperationIdPK pk = new OperationIdPK(opId.getSpace(), opId.getId());
			rv.setOperationIdPK(pk);
		}

		if (input.getItem() != null) {
			Item tempItem = input.getItem();
			if (tempItem.getItemId() != null) {
				ItemIdBoundary itId = tempItem.getItemId();
				rv.setItemSpace(itId.getSpace());
				rv.setItemId(itId.getId());
			}
		}

		if (input.getInvokedBy() != null) {
			User tempUser = input.getInvokedBy();
			if (tempUser.getUserId() != null) {
				UserId usId = tempUser.getUserId();
				rv.setUserSpace(usId.getSpace());
				rv.setUserEmail(usId.getEmail());
			}
		}

		if (input.getType() != null)
			rv.setType(input.getType());

		if (input.getCreatedTimestamp() != null)
			rv.setCreatedTimestamp(input.getCreatedTimestamp());

		if (input.getOperationAttributes() != null)
			rv.setOperationAttributes(this.fromMapToJson(input.getOperationAttributes()));

		return rv;
	}

	@Override
	public ItemBoundary toBoundary(ItemEntity input) {
		ItemBoundary rv = new ItemBoundary();
		ItemIdBoundary itemId = new ItemIdBoundary();
		itemId.setId(input.getItemIdPK().getId());
		itemId.setSpace(input.getItemIdPK().getSpace());
		rv.setItemId(itemId);
		rv.setType(input.getType());
		rv.setName(input.getName());
		rv.setActive(input.isActive());
		rv.setCreatedTimestamp(input.getCreatedTimestamp());
		
		User createdBy = new User(new UserId(input.getUserSpace(),input.getUserEmail()));
		rv.setCreatedBy(createdBy);
		
		Location location = new Location(input.getLocationLat(),input.getLocationLng());
		rv.setLocation(location);
		rv.setItemAttributes(this.fromJsonToMap(input.getItemAttributes()));

		return rv;
	}

	@Override
	public ItemEntity toEntity(ItemBoundary input) {
		ItemEntity rv = new ItemEntity();

		if (input.getItemId() != null) {
			ItemIdPK itemIdPK = new ItemIdPK();
			itemIdPK.setId(input.getItemId().getId());
			itemIdPK.setSpace(input.getItemId().getSpace());
			rv.setItemIdPK(itemIdPK);
		}

		if (input.getType() != null)
			rv.setType(input.getType());

		if (input.getName() != null)
			rv.setName(input.getName());

		if (input.isActive() != null)
			rv.setActive(input.isActive());

		if (input.getCreatedTimestamp() != null)
			rv.setCreatedTimestamp(input.getCreatedTimestamp());

		if (input.getCreatedBy() != null) {
			rv.setUserEmail(input.getCreatedBy().getUserId().getEmail());
			rv.setUserSpace(input.getCreatedBy().getUserId().getSpace());
		}
		if (input.getLocation() != null) {
			rv.setLocationLat(input.getLocation().getLat());
			rv.setLocationLng(input.getLocation().getLng());
		}

		if (input.getItemAttributes() != null)
			rv.setItemAttributes(this.fromMapToJson(input.getItemAttributes()));

		return rv;
	}

	@Override
	public UserBoundary toBoundary(UserEntity input) {
		UserBoundary rv = new UserBoundary();
		rv.setUsername(input.getUsername());
		
		rv.setUserId(new UserId(input.getUserId().getSpace(),input.getUserId().getEmail()));
		rv.setAvatar(input.getAvatar());
		rv.setRole(input.getRole().name());

		return rv;
	}

	@Override
	public UserEntity toEntity(UserBoundary input) {
		UserEntity rv = new UserEntity();

		if (input.getUsername() != null)
			rv.setUsername(input.getUsername());

		if (input.getUserId() != null) {
			rv.setUserId(new UserIdPK(input.getUserId().getSpace(),input.getUserId().getEmail()));
		}

		if (input.getAvatar() != null)
			rv.setAvatar(input.getAvatar());

		if (input.getRole() != null)
			rv.setRole(UserRole.valueOf(input.getRole()));

		return rv;
	}

	@Override
	public String fromMapToJson (Map<String, Object> value) { // marshalling: Java->JSON
		try {
			return this.jackson
				.writeValueAsString(value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	@Override
	public Map<String, Object> fromJsonToMap (String json) { // unmarshalling: JSON->Java
		try {
			return this.jackson
				.readValue(json, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
