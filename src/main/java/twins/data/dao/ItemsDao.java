package twins.data.dao;

import org.springframework.data.repository.CrudRepository;

import twins.data.ItemEntity;
import twins.data.ItemIdPK;

public interface ItemsDao extends CrudRepository<ItemEntity, ItemIdPK>{

}
