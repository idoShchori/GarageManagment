package twins.logic;

import twins.data.OperationEntity;
import twins.operations.OperationBoundary;

public interface EntityConverter {
	
	public OperationBoundary toBoundary(OperationEntity oe);
	
	public OperationEntity toEntity(OperationBoundary ob);
	
//	TODO: add ItemBoundary converter
//	TODO: add UserBoundary converter
//	TODO: add ItemBoundary converter
	
}
