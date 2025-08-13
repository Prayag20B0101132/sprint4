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

public class DeletePassengerById extends BaseTest {

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
    public void testDeletePassengerById(int id) {

        PassengerClient client = new PassengerClient(accessToken);
        SoftAssert softAssert = new SoftAssert();

        // Get list before deletion
        List<Integer> idsBefore = validIds(new JSONArray(client.viewPassengerList().asString()));

        test = extent.createTest("Delete Passenger By ID Test - ID: " + id);

        // Perform delete (returns updated list)
        Response response = client.deletePassengerById(id);
        test.info("Status Code: " + response.statusCode());
        test.info("Response: " + response.asPrettyString());

        // Extract IDs from updated list in delete response
        List<Integer> idsAfter = validIds(new JSONArray(response.asString()));

        if (idsBefore.contains(id)) {
            // Passenger existed before -> it should be gone now
            softAssert.assertEquals(response.statusCode(), 200,
                    "Expected 200 when deleting existing passenger");
            if (idsAfter.contains(id)) {
                test.fail("Passenger with ID: " + id + " still present after deletion.");
                softAssert.fail("Passenger still present after deletion.");
            } else {
                test.pass("Passenger with ID: " + id + " deleted successfully.");
            }

        } else {
            // Passenger did not exist before -> expect error
            if (response.statusCode() == 200) {
                test.fail("Delete returned 200 for non-existent passenger ID: " + id);
                softAssert.fail("Unexpected success for non-existent passenger");
            } else {
                test.pass("Non-existent passenger deletion handled correctly for ID: " + id);
            }
        }

        // At the end, report all soft assertion failures
        softAssert.assertAll();
    }
}
