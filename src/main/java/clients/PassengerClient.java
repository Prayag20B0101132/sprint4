package clients;

import config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class PassengerClient {
	private String token;
	
	public PassengerClient(String token) {
		this.token = token;
	}
	
	public Response addPassenger(int id, String name, String mobile, String gender, String aadhar, String address)
	{
		return RestAssured
				.given()
					.baseUri(ConfigManager.get("passenger.base.url"))
					.auth().oauth2(token)
					.accept("application/json")
					.contentType("application/x-www-form-urlencoded")
					.formParam("passengerId", id)
					.formParam("passengerName", name)
					.formParam("passengerMobile", mobile)
					.formParam("passengerGender", gender)
					.formParam("passengerAadhaarNumber", aadhar)
					.formParam("passengerAddress", address)
				.when()
					.post("/addPassenger");
	
	}
	
	public Response viewPassengerById(int id)
	{
		return RestAssured
				.given()
					.baseUri(ConfigManager.get("passenger.base.url"))
					.auth().oauth2(token)
					.accept("application/json")
				.when()
					.get("/viewPassengerById/" + id);
	}
	
	public Response deletePassengerById(int id){
		{
			return RestAssured
					.given()
						.baseUri(ConfigManager.get("passenger.base.url"))
						.auth().oauth2(token)
						.accept("application/json")
					.when()
						.delete("/deletePassengerById/" + id);
		}
	}
	
	public Response viewPassengerByNameAndMobile(String name, String mobile)
	{
		return RestAssured
				.given()
					.baseUri(ConfigManager.get("passenger.base.url"))
					.auth().oauth2(token)
					.accept("application/json")
				.when()
					.get("/viewPassengerByNameMobile/" + name + "/" + mobile);
	}
	
	public Response viewPassengerList()
	{
		return RestAssured
				.given()
					.baseUri(ConfigManager.get("passenger.base.url"))
					.auth().oauth2(token)
					.accept("application/json")
				.when()
					.get("/viewPassengerList");
	}
}
