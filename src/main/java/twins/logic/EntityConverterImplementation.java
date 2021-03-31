package twins.logic;

import org.springframework.stereotype.Component;

import twins.data.OperationEntity;
import twins.items.Item;
import twins.items.ItemId;
import twins.operations.OperationBoundary;
import twins.operations.OperationId;
import twins.users.User;
import twins.users.UserId;

@Component
public class EntityConverterImplementation implements EntityConverter {

	@Override
	public OperationBoundary toBoundary(OperationEntity oe) {
		OperationBoundary output = new OperationBoundary();

		OperationId opId = new OperationId(
				oe.getOperationSpace(),
				oe.getOperationId());
		output.setOperationId(opId);

		ItemId itId = new ItemId(
				oe.getItemSpace(), 
				oe.getItemId());
		output.setItem(new Item(itId));

		UserId usId = new UserId(
				oe.getUserSpace(), 
				oe.getUserEmail());
		output.setInvokedBy(new User(usId));

		output.setType(oe.getType());

		output.setCreatedTimestamp(oe.getCreatedTimestamp());

		output.setOperationAttributes(oe.getOperationAttributes());

		return output;
	}

	@Override
	public OperationEntity toEntity(OperationBoundary ob) {
		OperationEntity output = new OperationEntity();
		
		OperationId opId = ob.getOperationId();
		output.setOperationSpace(opId.getSpace());
		output.setOperationId(opId.getId());
		
		ItemId itId = ob.getItem().getItemId();
		output.setItemSpace(itId.getSpace());
		output.setItemId(itId.getId());
		
		UserId usId = ob.getInvokedBy().getUserId();
		output.setUserSpace(usId.getSpace());
		output.setUserEmail(usId.getEmail());
		
		output.setType(ob.getType());
		
		output.setCreatedTimestamp(ob.getCreatedTimestamp());
		
		output.setOperationAttributes(ob.getOperationAttributes());
		
		return output;
	}

//	TODO: add ItemBoundary converter
//	TODO: add UserBoundary converter
//	TODO: add ItemBoundary converter

}
