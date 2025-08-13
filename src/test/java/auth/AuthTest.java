package auth;

import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import clients.AuthClient;
import io.restassured.response.Response;
public class AuthTest extends BaseTest {
	
	@Test
	public void testAuthCode() {
		Response response = AuthClient.verifyAccessToken(accessToken);
		test = extent.createTest("Verify Access Token");
		
		test.info("Status Code: " + response.statusCode());
		test.info("Response: " + response.asPrettyString());
		
		Assert.assertEquals(response.statusCode(),200);
		test.pass("Token successfully verified.");
		response.then().assertThat().body("active", equalTo(true));
		test.pass("Token is active");
		System.out.println(response.asPrettyString());
	}
}
