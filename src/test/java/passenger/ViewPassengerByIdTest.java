package passenger;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseTest;
import clients.PassengerClient;
import io.restassured.response.Response;
import pojo.IdWrapper;
import util.JsonReader;

public class ViewPassengerByIdTest extends BaseTest {

    public static List<Integer> validIds(JSONArray array) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ids.add(obj.getInt("passengerId"));
        }
        return ids;
    }

    @DataProvider(name = "id")
    public Object[][] idData() {
        List<IdWrapper> idList = JsonReader.readJSON(
                "src/main/resources/testdata/ids.json",
                IdWrapper.class
        );
        Object[][] arr = new Object[idList.size()][1];
        for (int i = 0; i < idList.size(); i++) {
            arr[i][0] = idList.get(i).getId();
        }
        return arr;
    }

    @Test(dataProvider = "id")
    public void testViewPassengerById(int id) {

        PassengerClient client = new PassengerClient(accessToken);
        SoftAssert softAssert = new SoftAssert();

        // Initialize ExtentReports test
        test = extent.createTest("View Passenger By ID Test - ID: " + id);

        // Get list before viewing
        List<Integer> idsBefore = validIds(new JSONArray(client.viewPassengerList().asString()));

        // Call the API
        Response response = client.viewPassengerById(id);
        test.info("Status Code: " + response.statusCode());
        test.info("Response: " + response.asPrettyString());

        if (idsBefore.contains(id)) {
            // Existing passenger: expect 200
            softAssert.assertEquals(response.statusCode(), 200,
                    "Expected status 200 for existing passenger");
            if (!response.asString().contains(String.valueOf(id))) {
                test.fail("Response does not contain expected details for ID: " + id);
                softAssert.fail("Passenger details missing in response for ID: " + id);
            } else {
                test.pass("Response contains details of passenger with ID: " + id);
            }
        } else {
            // Non-existent passenger: should NOT return 200
            if (response.statusCode() == 200) {
                test.fail("Get returned 200 for non-existent passenger ID: " + id);
                softAssert.fail("Unexpected success for non-existent passenger ID: " + id);
            } else {
                test.pass("Non-existent passenger ID: " + id + " handled correctly with status " + response.statusCode());
            }
        }

        // Report all failures at the end
        softAssert.assertAll();
    }
}
