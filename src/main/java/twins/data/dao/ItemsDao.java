package twins.data.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import twins.data.ItemEntity;
import twins.data.ItemIdPK;

public interface ItemsDao extends PagingAndSortingRepository<ItemEntity, ItemIdPK> {

}
