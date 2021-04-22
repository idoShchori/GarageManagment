package twins.logic.logicImplementation;

import java.util.Map;

import twins.data.ItemEntity;
import twins.data.OperationEntity;
import twins.data.UserEntity;
import twins.items.ItemBoundary;
import twins.operations.OperationBoundary;
import twins.users.UserBoundary;


public interface EntityConverter {
	
	public OperationBoundary toBoundary(OperationEntity oe);
	public OperationEntity toEntity(OperationBoundary ob);
	
	public ItemBoundary toBoundary(ItemEntity oe);
	public ItemEntity toEntity(ItemBoundary ob);
	
	public UserBoundary toBoundary(UserEntity oe);
	public UserEntity toEntity(UserBoundary ob);
	
	public String fromMapToJson (Map<String, Object> value);
	public Map<String, Object> fromJsonToMap (String json);

}
