package twins.data.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import twins.data.OperationEntity;
import twins.data.OperationIdPK;

public interface OperationsDao extends PagingAndSortingRepository<OperationEntity, OperationIdPK>  {

}