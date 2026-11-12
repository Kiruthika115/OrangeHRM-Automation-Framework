package com.orangehrmproj.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrmproj.base.BaseClass;
import com.orangehrmproj.pages.DashboardPage;
import com.orangehrmproj.pages.LoginPage;

public class LoginTest extends BaseClass {

    @Test(priority = 1)
    public void validLoginTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");

        DashboardPage dashboard = new DashboardPage(driver);
        Assert.assertTrue(dashboard.isDashboardDisplayed(), "Dashboard not displayed after login");
    }

    @Test(priority = 2)
    public void invalidLoginTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "wrongpass");

        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials");
    }
}