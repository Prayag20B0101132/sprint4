package passenger;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import clients.PassengerClient;
import io.restassured.response.Response;

public class ViewPassengerList extends BaseTest {

    @Test
    public void testViewPassengerList() {
        PassengerClient client = new PassengerClient(accessToken);

        // Create ExtentReports test entry
        test = extent.createTest("View Passenger List Test");

        // Call API
        Response response = client.viewPassengerList();

        // Log details
        test.info("Status Code: " + response.statusCode());
        test.info("Response: " + response.asPrettyString());

        // Validate status code
        Assert.assertEquals(response.statusCode(), 200, "Expected status code 200");

        // Validate that response is not empty
        String responseBody = response.asString();
        Assert.assertTrue(responseBody.contains("passengerId") || responseBody.contains("passengerName"),
                "Passenger list appears to be empty or missing expected fields");

        test.pass("Passenger list retrieved successfully and contains data.");
    }
}
