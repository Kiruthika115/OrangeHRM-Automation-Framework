package com.orangehrmproj.tests;

import static io.restassured.RestAssured.given;

import org.openqa.selenium.Cookie;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrmproj.base.BaseClass;
import com.orangehrmproj.pages.LoginPage;
import com.orangehrmproj.pages.PIMPage;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ApiVsUiTest extends BaseClass {

    @Test
    public void employeeDataMatchesBetweenUiAndApi() {
        String first = "Priya";
        String last = "Devi" + (System.currentTimeMillis() % 100000);

        // 1. Create the employee through the UI, then verify it in the UI
        new LoginPage(driver).login("Admin", "admin123");
        PIMPage pim = new PIMPage(driver);
        pim.openPIM();
        pim.addEmployee(first, last);
        pim.searchEmployee(first + " " + last);
        String uiFirst = pim.getFirstResultFirstName();
        String uiLast = pim.getFirstResultLastName();

        // 2. Reuse the browser session for the API call
        Cookie session = driver.manage().getCookieNamed("orangehrm");

        Response response = given()
                .baseUri("https://opensource-demo.orangehrmlive.com")
                .cookie("orangehrm", session.getValue())
                .queryParam("nameOrId", first + " " + last)
                .queryParam("limit", 50)
                .queryParam("offset", 0)
        .when()
                .get("/web/index.php/api/v2/pim/employees")
        .then()
                .statusCode(200)
                .extract().response();

        JsonPath json = response.jsonPath();

        // 3. Validate the API response, then compare it with the UI
        Assert.assertEquals(json.getInt("meta.total"), 1, "API should return exactly one employee");
        Assert.assertEquals(json.getString("data[0].firstName"), first);
        Assert.assertEquals(json.getString("data[0].lastName"), last);

        Assert.assertEquals(uiFirst, json.getString("data[0].firstName"), "UI vs API first name");
        Assert.assertEquals(uiLast, json.getString("data[0].lastName"), "UI vs API last name");
    }
}