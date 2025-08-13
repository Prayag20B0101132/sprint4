package passenger;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseTest;
import clients.PassengerClient;
import io.restassured.response.Response;
import pojo.Passenger;
import util.JsonReader;

public class AddPassengerTest extends BaseTest {

    @DataProvider(name = "passenger")
    public Object[][] passengerData() {
        List<Passenger> passengerList =
                JsonReader.readJSON("src/main/resources/testdata/addPassenger.json", Passenger.class);

        Object[][] arr = new Object[passengerList.size()][6];
        for (int i = 0; i < passengerList.size(); i++) {
            arr[i][0] = passengerList.get(i).getPassengerId();
            arr[i][1] = passengerList.get(i).getPassengerName();
            arr[i][2] = passengerList.get(i).getPassengerMobile();
            arr[i][3] = passengerList.get(i).getPassengerGender();
            arr[i][4] = passengerList.get(i).getPassengerAadhaarNumber();
            arr[i][5] = passengerList.get(i).getPassengerAddress();
        }
        return arr;
    }

    @Test(dataProvider = "passenger")
    public void testAddPassenger(int id, String name, String mobile, String gender,
                                 String aadhaarNumber, String address) {

        PassengerClient client = new PassengerClient(accessToken);
        test = extent.createTest("Add Passenger Test - ID: " + id);
        SoftAssert softAssert = new SoftAssert();

        // Snapshot BEFORE
        JSONArray before = new JSONArray(client.viewPassengerList().asString());
        boolean existedBefore = containsId(before, id);
        int countBefore = countId(before, id);

        // POST
        Response response = client.addPassenger(id, name, mobile, gender, aadhaarNumber, address);
        test.info("Status Code: " + response.statusCode());
        test.info("Response: " + response.asPrettyString());

        // Snapshot AFTER
        JSONArray after = new JSONArray(response.asString());
        boolean existsAfter = containsId(after, id);
        int countAfter = countId(after, id);

        // Case 1: Missing fields
        if (isBlank(name) || isBlank(mobile) || isBlank(gender) || isBlank(aadhaarNumber) || isBlank(address)) {
            softAssert.assertEquals(response.statusCode(), 400,
                    "Expected 400 Bad Request for empty fields");
            softAssert.assertEquals(countAfter, countBefore,
                    "Invalid passenger should not be added to list");
            if (response.statusCode() != 400 || countAfter != countBefore) {
                test.fail("Empty/invalid fields not handled correctly");
            } else {
                test.pass("Empty/invalid fields correctly rejected with 400 status");
            }
            softAssert.assertAll();
            return;
        }

        // Case 2: Duplicate ID
        if (existedBefore) {
            softAssert.assertEquals(response.statusCode(), 409,
                    "Expected 409 Conflict for duplicate ID");
            softAssert.assertEquals(countAfter, countBefore,
                    "List changed unexpectedly on duplicate ID");
            if (response.statusCode() != 409 || countAfter != countBefore) {
                test.fail("Duplicate ID not handled correctly");
            } else {
                test.pass("Duplicate ID correctly rejected with 409 status");
            }
            softAssert.assertAll();
            return;
        }

        // Case 3: Happy path
        softAssert.assertEquals(response.statusCode(), 200,
                "Expected 200 for successful add");
        softAssert.assertTrue(existsAfter,
                "Passenger ID should be present after successful add");
        softAssert.assertEquals(countAfter, countBefore + 1,
                "Passenger list count for this ID should increase by 1");

        if (response.statusCode() != 200 || !existsAfter || countAfter != countBefore + 1) {
            test.fail("Passenger add failed validation checks");
        } else {
            test.pass("Passenger added successfully: " + id);
        }

        // Fail at the end if any soft assertions failed
        softAssert.assertAll();
    }

    // ---- helpers ----
    private static boolean containsId(JSONArray array, int id) {
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if (obj.optInt("passengerId", Integer.MIN_VALUE) == id) {
                return true;
            }
        }
        return false;
    }

    private static int countId(JSONArray array, int id) {
        int count = 0;
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if (obj.optInt("passengerId", Integer.MIN_VALUE) == id) {
                count++;
            }
        }
        return count;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
