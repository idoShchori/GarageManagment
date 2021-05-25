package twins.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import twins.data.ItemEntity;
import twins.data.ItemIdPK;

public interface ItemsDao extends PagingAndSortingRepository<ItemEntity, ItemIdPK> {
	public List<ItemEntity> findAllByParent_itemIdPK(
			@Param("parentId") ItemIdPK parentId,
			Pageable pageable);
	
	public List<ItemEntity> findAllByChildren_itemIdPK(
			@Param("childId") ItemIdPK childId,
			Pageable pageable);
	
	public List<ItemEntity> findAllByTypeAndCreatedTimestamp(
			@Param("type") String type,
			@Param("createdTimestamp") Date createdTimestamp,
			Pageable pageable);
	
	public List<ItemEntity> findAllByTypeAndCreatedTimestampBetween(
			@Param("type") String type, 
			@Param("startdate") Date startdate,
			@Param("enddate") Date enddate);
	
	public List<ItemEntity> findAllByCreatedTimestampBetween(
			@Param("startdate") Date startdate,
			@Param("enddate") Date enddate);
	
	public List<ItemEntity> findAllByTypeAndActive(
			@Param("type") String type, 
			@Param("active") boolean active,
			Pageable pageable);	
}
