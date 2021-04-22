package twins.data.dao;

import org.springframework.data.repository.CrudRepository;

import twins.data.OperationEntity;

public interface OperationsDao extends CrudRepository<OperationEntity, String> {

}