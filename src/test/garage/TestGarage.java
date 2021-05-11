package garage;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import twins.Application;
import twins.users.UserBoundary;



@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.RANDOM_PORT)
public class TestGarage {
	private int port;
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port=port;
	}
	
	@PostConstruct
	public void initConstruct() {
		this.restTemplate = new RestTemplate();
	}
	
	
	@Test
	public void testContext() throws Exception{
		// given the server is up
		
		// when the application initializes
		
		// then spring starts up with no errors.
	}
	
	//TODO: add actual tests
	@Test //@Disabled
	public void testPostUserOnServer() throws Exception{
		
		
		String theUrl = "http://localhost:" + this.port + "/twins/users";
		Map<String,Object> emptyUser = new HashMap<>();
		
		UserBoundary user=this.restTemplate.postForObject(theUrl, emptyUser, UserBoundary.class);
		
		//assert that the id is not null AND returns message with initialized unique id
		assertThat(user.getUserId()).overridingErrorMessage("Expected user id").isNotNull();
		
		assertThat(user.getUsername()).overridingErrorMessage("Expected user name").isNotNull();
		
		assertThat(user.getRole()).overridingErrorMessage("Expected role").isNotNull();
		
		assertThat(user.getAvatar()).overridingErrorMessage("Expected avatar").isNotNull();
	}
	
}
