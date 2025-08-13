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
import pojo.Contact;
import util.JsonReader;

public class ViewPassengerByNameAndMobile extends BaseTest {

    public static List<String> validData(JSONArray array, String key) {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            values.add(obj.getString(key));
        }
        return values;
    }

    @DataProvider(name = "nameAndMobile")
    public Object[][] nameAndMobileData() {
        List<Contact> nameMobileList = JsonReader.readJSON(
                "src/main/resources/testdata/passengerNameAndMobile.json",
                Contact.class
        );
        Object[][] arr = new Object[nameMobileList.size()][2];
        for (int i = 0; i < nameMobileList.size(); i++) {
            arr[i][0] = nameMobileList.get(i).getName();
            arr[i][1] = nameMobileList.get(i).getMobile();
        }
        return arr;
    }

    @Test(dataProvider = "nameAndMobile")
    public void testViewPassengerByNameMobile(String name, String mobile) {
        PassengerClient client = new PassengerClient(accessToken);
        SoftAssert softAssert = new SoftAssert();

        // Create ExtentReports entry
        test = extent.createTest(
                "View Passenger By Name & Mobile Test - Name: " + name + ", Mobile: " + mobile
        );

        // Get full passenger list before API call
        JSONArray passengerArray = new JSONArray(client.viewPassengerList().asString());

        // Call API
        Response response = client.viewPassengerByNameAndMobile(name, mobile);
        test.info("Status Code: " + response.statusCode());
        test.info("Response: " + response.asPrettyString());

        // Check if passenger exists in the current list
        boolean exists = false;
        for (int i = 0; i < passengerArray.length(); i++) {
            JSONObject obj = passengerArray.getJSONObject(i);
            if (obj.getString("passengerName").equals(name) &&
                obj.getString("passengerMobile").equals(mobile)) {
                exists = true;
                break;
            }
        }

        if (exists) {
            // Expect status 200
            softAssert.assertEquals(response.statusCode(), 200,
                    "Expected status 200 for existing passenger");
            if (!(response.asString().contains(name) && response.asString().contains(mobile))) {
                test.fail("Response missing correct passenger details for " + name + " / " + mobile);
                softAssert.fail("Passenger details missing for " + name + " / " + mobile);
            } else {
                test.pass("Response contains correct passenger details for " + name + " / " + mobile);
            }
        } else {
            // Non-existent passenger: should not return 200
            if (response.statusCode() == 200) {
                test.fail("Unexpected 200 for non-existent passenger " + name + " / " + mobile);
                softAssert.fail("Unexpected success for non-existent passenger " + name + " / " + mobile);
            } else {
                test.pass("Non-existent passenger " + name + " / " + mobile + " handled correctly with status " + response.statusCode());
            }
        }

        // Report all collected failures
        softAssert.assertAll();
    }
}
