package twins.logic.logicImplementation;

import org.springframework.data.repository.CrudRepository;

import twins.data.ItemEntity;

public interface ItemsDao extends CrudRepository<ItemEntity, String>{

}
