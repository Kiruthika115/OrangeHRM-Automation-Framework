
package com.orangehrmproj.reports;

import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.orangehrmproj.base.BaseClass;
import com.orangehrmproj.utilities.ScreenshotUtils;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {

        ExtentReportManager.createTest(
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        ExtentReportManager.test.pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        ExtentReportManager.test.fail(result.getThrowable());

        // Get WebDriver from BaseClass
        WebDriver driver =
                ((BaseClass) result.getInstance()).driver;

        if (driver != null) {

            String screenshotPath =
                    ScreenshotUtils.captureScreenshot(
                            driver,
                            result.getMethod().getMethodName()
                                    + "_" + System.currentTimeMillis());

            try {
                ExtentReportManager.test.addScreenCaptureFromPath(
                        screenshotPath);

                System.out.println(
                        "Failure screenshot attached: " + screenshotPath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        ExtentReportManager.test.skip("Test Skipped");
    }
}
