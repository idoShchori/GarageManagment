package twins.logic.logicImplementation.useCases;

import org.springframework.beans.factory.annotation.Autowired;

import twins.logic.UpdatedItemsService;
import twins.logic.UpdatedUsersService;
import twins.logic.logicImplementation.EntityConverter;
import twins.logic.logicImplementation.Validator;

public abstract class AbstractUseCase {

	protected UpdatedItemsService itemsService;
	protected UpdatedUsersService usersService;
	protected EntityConverter entityConverter;
	protected Validator validator;
	
	@Autowired
	public void setItemService(UpdatedItemsService itemService) {
		this.itemsService = itemService;
	}

	@Autowired
	public void setUsersService(UpdatedUsersService usersService) {
		this.usersService = usersService;
	}
	
	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	
}
