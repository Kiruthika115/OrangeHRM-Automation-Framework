package com.orangehrmproj.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangehrmproj.base.BaseClass;
import com.orangehrmproj.pages.DashboardPage;
import com.orangehrmproj.pages.LoginPage;
import com.orangehrmproj.utilities.ExcelUtils;

public class LoginDataTest extends BaseClass {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return ExcelUtils.getData("testdata/LoginData.xlsx", "LoginData");
    }

    @Test(dataProvider = "loginData")
    public void loginWithExcelData(String username, String password, String expected) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        if (expected.equals("success")) {
            Assert.assertTrue(new DashboardPage(driver).isDashboardDisplayed());
        } else if (expected.equals("Required")) {
            Assert.assertTrue(loginPage.getRequiredMessage().contains("Required"));
        } else {
            Assert.assertEquals(loginPage.getErrorMessage(), expected);
        }
    }
}