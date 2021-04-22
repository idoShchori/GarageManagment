package twins.logic.logicImplementation;

import org.springframework.stereotype.Component;

import twins.data.ItemEntity;
import twins.data.OperationEntity;
import twins.data.UserEntity;
import twins.data.UserIdPK;
import twins.data.UserRole;
import twins.items.Item;
import twins.items.ItemBoundary;
import twins.items.ItemId;
import twins.operations.OperationBoundary;
import twins.operations.OperationId;
import twins.users.User;
import twins.users.UserBoundary;
import twins.users.UserId;

@Component
public class EntityConverterImplementation implements EntityConverter {

	@Override
	public OperationBoundary toBoundary(OperationEntity input) {
		OperationBoundary rv = new OperationBoundary();

		OperationId opId = new OperationId(input.getOperationSpace(), input.getOperationId());
		rv.setOperationId(opId);

		ItemId itId = new ItemId(input.getItemSpace(), input.getItemId());
		rv.setItem(new Item(itId));

		UserId usId = new UserId(input.getUserSpace(), input.getUserEmail());
		rv.setInvokedBy(new User(usId));

		rv.setType(input.getType());

		rv.setCreatedTimestamp(input.getCreatedTimestamp());

		rv.setOperationAttributes(input.getOperationAttributes());

		return rv;
	}

	@Override
	public OperationEntity toEntity(OperationBoundary input) {
		OperationEntity rv = new OperationEntity();

		if (input.getOperationId() != null) {
			OperationId opId = input.getOperationId();
			rv.setOperationSpace(opId.getSpace());
			rv.setOperationId(opId.getId());
		}

		if (input.getItem() != null) {
			Item tempItem = input.getItem();
			if (tempItem.getItemId() != null) {
				ItemId itId = tempItem.getItemId();
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
			rv.setOperationAttributes(input.getOperationAttributes());

		return rv;
	}

	@Override
	public ItemBoundary toBoundary(ItemEntity input) {
		ItemBoundary rv = new ItemBoundary();

		rv.setItemId(input.getItemId());
		rv.setType(input.getType());
		rv.setName(input.getName());
		rv.setActive(input.isActive());
		rv.setCreatedTimestamp(input.getCreatedTimestamp());
		rv.setCreatedBy(input.getCreatedBy());
		rv.setLocation(input.getLocation());
		rv.setItemAttributes(input.getItemAttributes());

		return rv;
	}

	@Override
	public ItemEntity toEntity(ItemBoundary input) {
		ItemEntity rv = new ItemEntity();

		if (input.getItemId() != null)
			rv.setItemId(input.getItemId());

		if (input.getType() != null)
			rv.setType(input.getType());

		if (input.getName() != null)
			rv.setName(input.getName());

		if (input.isActive() != null)
			rv.setActive(input.isActive());

		if (input.getCreatedTimestamp() != null)
			rv.setCreatedTimestamp(input.getCreatedTimestamp());

		if (input.getCreatedBy() != null)
			rv.setCreatedBy(input.getCreatedBy());

		if (input.getLocation() != null)
			rv.setLocation(input.getLocation());

		if (input.getItemAttributes() != null)
			rv.setItemAttributes(input.getItemAttributes());

		return rv;
	}

	@Override
	public UserBoundary toBoundary(UserEntity input) {
		UserBoundary rv = new UserBoundary();
		//TODO check that split is WORKING!!!
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


}
