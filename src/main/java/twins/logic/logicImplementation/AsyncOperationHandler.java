package twins.logic.logicImplementation;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import twins.data.OperationEntity;
import twins.data.dao.OperationsDao;
import twins.operations.OperationBoundary;

@Component
public class AsyncOperationHandler {
	private ObjectMapper jackson;
	private EntityConverter entityConverter;
	private OperationsDao operationsDao;

	public AsyncOperationHandler() {
		this.jackson = new ObjectMapper();
	}

	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
	}

	@Autowired
	public void setOperationsDao(OperationsDao operationsDao) {
		this.operationsDao = operationsDao;
	}
	
	@Transactional
	@JmsListener(destination = "asyncInbox")
	public void handleJson(String json) {
		try {
			System.err.println("waiting 3s to handle: " + json);
			Thread.sleep(3L * 1000); // NOTE: this stalls handling the rest of processing for 3 whole seconds - just for demo sake
			
			OperationBoundary boundary = this.jackson
											.readValue(json, OperationBoundary.class);
			
			boundary.setCreatedTimestamp(new Date());
			
			//	TODO: do something async
			
			OperationEntity entity = this.entityConverter.toEntity(boundary);
			this.operationsDao.save(entity);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
