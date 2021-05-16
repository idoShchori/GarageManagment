package twins.logic;

import java.util.List;

import twins.operations.OperationBoundary;

public interface OperationsService {
	public Object invokeOperation(OperationBoundary operation,int page, int size);
	public OperationBoundary invokeAsynchronous(OperationBoundary operation);
	@Deprecated
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail);
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail, int size, int page);
	public void deleteAllOperations(String adminSpace, String adminEmail);
}
