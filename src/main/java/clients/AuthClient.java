package clients;

import java.util.Map;

import config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
public class AuthClient {
	public static String getAccessToken() {
		String baseUrl = ConfigManager.get("auth.base.url");
		String endpoint = ConfigManager.get("auth.token.endpoint");
		
		Response response = RestAssured
				.given()
					.baseUri(baseUrl)
					.accept("application/json")
					.contentType("application/x-www-form-urlencoded")
					.formParam("grant_type", ConfigManager.get("auth.grant.type"))
					.formParam("client_id", ConfigManager.get("auth.client.id"))
					.formParam("client_secret", ConfigManager.get("auth.client.secret"))
				.when()
					.post(endpoint)
				.then()
					.statusCode(200)
				.extract()
					.response();
		return response.jsonPath().getString("access_token");
								
	}
	
	public static Response verifyAccessToken(String access_token) {
		String baseUrl = ConfigManager.get("auth.verify.url");
		String endpoint = ConfigManager.get("auth.verify.endpoint");
		Map<String,String> parameters = Map.
										of("grant_type",ConfigManager.get("auth.grant.type")
										,"client_id",ConfigManager.get("auth.client.id")
										,"client_secret",ConfigManager.get("auth.client.secret")
										,"token",access_token);
		return RestAssured
				.given()
					.baseUri(baseUrl)
					.accept("application/json")
					.contentType(ContentType.URLENC)
					.formParams(parameters)
				.when()
					.post(endpoint)
				.then()
					.statusCode(200)
				.extract()
					.response();
	}
}
