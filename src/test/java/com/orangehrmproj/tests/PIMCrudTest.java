package com.orangehrmproj.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrmproj.base.BaseClass;
import com.orangehrmproj.pages.LoginPage;
import com.orangehrmproj.pages.PIMPage;

public class PIMCrudTest extends BaseClass {

    @Test
    public void editAndDeleteEmployee() {
        String first = "Divya";
        String last = "Nair" + System.currentTimeMillis();
        String updatedLast = "Iyer" + System.currentTimeMillis();

        new LoginPage(driver).login("Admin", "admin123");
        PIMPage pim = new PIMPage(driver);

        // Create
        pim.openPIM();
        pim.addEmployee(first, last);

        // Update
        pim.openPIM();
        pim.searchEmployee(first + " " + last);
        pim.editLastName(updatedLast);

        // Read: verify the update
        pim.openPIM();
        pim.searchEmployee(first + " " + updatedLast);
        Assert.assertEquals(pim.getFirstResultLastName(), updatedLast, "Last name was not updated");

        // Delete, then verify it is gone
        pim.deleteFirstResult();
        pim.searchEmployeeExpectNone(first + " " + updatedLast);
        Assert.assertTrue(pim.isNoRecordsFound(), "Deleted employee still appears in the list");
    }
}