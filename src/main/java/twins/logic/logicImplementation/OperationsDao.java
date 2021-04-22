package twins.logic.logicImplementation;

import org.springframework.data.repository.CrudRepository;
import twins.data.OperationEntity;

public interface OperationsDao extends CrudRepository<OperationEntity, String> {

}