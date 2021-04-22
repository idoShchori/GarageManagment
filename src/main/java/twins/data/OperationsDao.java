package twins.data;

import org.springframework.data.repository.CrudRepository;

public interface OperationsDao extends CrudRepository<OperationEntity, String> {

}