
package com.orangehrmproj.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.orangehrmproj.base.BaseClass;
import com.orangehrmproj.pages.LoginPage;
import com.orangehrmproj.pages.PIMPage;
import com.orangehrmproj.reports.TestListener;
import com.orangehrmproj.utilities.ExcelUtils;

@Listeners(TestListener.class)
public class PIMTest extends BaseClass {

    @DataProvider(name = "employeeComparisonData")
    public Object[][] employeeComparisonData() {

        Object[][] creationData = ExcelUtils.getData(
            "testdata/EmployeeData.xlsx", "EmployeeData");

        Object[][] expectedData = ExcelUtils.getData(
            "testdata/ExpectedEmployeeData.xlsx", "EmployeeData");

        if (creationData.length != expectedData.length) {
            throw new RuntimeException(
                "Both Excel files must contain the same number of data rows.");
        }

        Object[][] combinedData =
            new Object[creationData.length][4];

        for (int i = 0; i < creationData.length; i++) {

            combinedData[i][0] = creationData[i][0];
            combinedData[i][1] = creationData[i][1];

            combinedData[i][2] = expectedData[i][0];
            combinedData[i][3] = expectedData[i][1];
        }

        return combinedData;
    }

    @Test(dataProvider = "employeeComparisonData")
    public void createAndValidateEmployee(
            String createFirstName,
            String createLastName,
            String expectedFirstName,
            String expectedLastName) {

        // Step 1: Generate a unique last name for every execution
        String uniqueSuffix =
            String.valueOf(System.currentTimeMillis());

        String uniqueLastName = createLastName + uniqueSuffix;

        // Step 2: Login to OrangeHRM
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("Admin", "admin123");

        // Step 3: Open PIM
        PIMPage pimPage = new PIMPage(driver);

        pimPage.openPIM();

        // Step 4: Create employee using Excel 1
        pimPage.addEmployee(
            createFirstName,
            uniqueLastName);

        System.out.println(
            "Employee created: " +
            createFirstName + " " + uniqueLastName);

        // Step 5: Search the employee saved in OrangeHRM
        pimPage.searchEmployee(
            createFirstName + " " + uniqueLastName);

        // Step 6: Verify employee exists
        int resultCount = pimPage.getResultCount();

        Assert.assertEquals(
            resultCount,
            1,
            "Employee record not found or duplicate records returned.");

        // Step 7: Read actual values from OrangeHRM UI
        String actualFirstName =
            pimPage.getFirstResultFirstName();

        String actualLastName =
            pimPage.getFirstResultLastName();

        System.out.println("Actual UI First Name: " + actualFirstName);
        System.out.println("Actual UI Last Name: " + actualLastName);

        // Step 8: Prepare expected last name with generated suffix
        String expectedUniqueLastName =
            expectedLastName + uniqueSuffix;

        // Step 9: Compare actual UI values against Excel 2
        Assert.assertEquals(
            actualFirstName,
            expectedFirstName,
            "First name mismatch! Expected from Excel 2: "
            + expectedFirstName + ", Actual UI: " + actualFirstName);

        Assert.assertEquals(
            actualLastName,
            expectedUniqueLastName,
            "Last name mismatch! Expected from Excel 2: "
            + expectedUniqueLastName + ", Actual UI: " + actualLastName);

        System.out.println(
            "Employee validation successful: " +
            actualFirstName + " " + actualLastName);
    }
}
