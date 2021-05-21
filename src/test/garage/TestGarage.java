package garage;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import twins.Application;
import twins.data.OperationEntity;
import twins.data.UserEntity;
import twins.items.ItemBoundary;
import twins.operations.OperationBoundary;
import twins.users.UserBoundary;

@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestGarage {
	private int port;
	private RestTemplate restTemplate;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void initConstruct() {
		this.restTemplate = new RestTemplate();
	}

	@Test
	public void testContext() throws Exception {
		// given the server is up

		// when the application initializes

		// then spring starts up with no errors.
	}

	@Test // @Disabled
	public void testPostUserOnServer() throws Exception {

		String theUrl = "http://localhost:" + this.port + "/twins/users";
		Map<String, Object> emptyUser = new HashMap<>();

		UserBoundary user = this.restTemplate.postForObject(theUrl, emptyUser, UserBoundary.class);

		// assert that the id is not null AND returns message with initialized unique id
		assertThat(user.getUserId()).overridingErrorMessage("Expected user id").isNotNull();

		assertThat(user.getUsername()).overridingErrorMessage("Expected user name").isNotNull();

		assertThat(user.getRole()).overridingErrorMessage("Expected role").isNotNull();

		assertThat(user.getAvatar()).overridingErrorMessage("Expected avatar").isNotNull();
	}

//	@Test
//	public ItemBoundary[] testPaginationOnItems() throws Exception{
//
//		
//		String theUrl = "http://localhost:" + this.port + "/twins/items/admin/admin?size=20&page=1";
//		List <ItemEntity> list = new ArrayList<ItemEntity>();
//		
//		ItemBoundary allItems =  this.restTemplate.getForObject(theUrl, ItemBoundary.class, list);
//		ItemsService itemService;
//		
//		return itemService.getAllItems("admin", "admin", 20, 1)
//				.toArray(new ItemBoundary[0]);
//	}

	@Test
	public void testPaginationOnItems() throws Exception {

		String theUrl = "http://localhost:" + this.port + "/twins/items/2021b.guy.kabiri/test@admin?size=20&page=1";
		List<ItemBoundary> itemsList = new ArrayList<ItemBoundary>();

		this.restTemplate.getForObject(theUrl, ItemBoundary.class, itemsList);

		System.out.println("---------------ITEMS LIST TEST---------------");
		System.out.println(itemsList);

	}

	@Test
	public void testPaginationOnUsers() throws Exception {

		String theUrl = "http://localhost:" + this.port + "/twins/users/2021b.guy.kabiri/test@admin?size=20&page=1";
		List<UserEntity> usersList = new ArrayList<UserEntity>();

		this.restTemplate.getForObject(theUrl, UserBoundary.class, usersList);

		System.out.println("---------------USERS LIST TEST---------------");
		System.out.println(usersList);

	}
	
	@Test
	public void testPaginationOnOperations() throws Exception {

		String theUrl = "http://localhost:" + this.port + "/twins/operations/2021b.guy.kabiri/test@admin?size=20&page=1";
		List<OperationEntity> operationsList=new ArrayList<OperationEntity>();

		this.restTemplate.getForObject(theUrl, OperationBoundary.class, operationsList);

		System.out.println("---------------OPERATIONS LIST TEST---------------");
		System.out.println(operationsList);

	}
	
	

}
