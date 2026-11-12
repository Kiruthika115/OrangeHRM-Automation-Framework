
package com.orangehrmproj.reports;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {

    public static ExtentReports extent;
    public static ExtentTest test;

    public static void initReport() {

        String projectPath = System.getProperty("user.dir");
        String reportFolder = projectPath + File.separator + "reports";

        new File(reportFolder).mkdirs();

        String reportPath = reportFolder + File.separator
                + "OrangeHRMReport.html";

        ExtentSparkReporter spark =
                new ExtentSparkReporter(reportPath);

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Project", "OrangeHRM Automation");
        extent.setSystemInfo("Framework", "Selenium + TestNG");
        extent.setSystemInfo("Language", "Java");

        System.out.println("Extent Report Initialized");
        System.out.println("Report path: " + reportPath);
    }

    public static void createTest(String testName) {
        test = extent.createTest(testName);
    }

    public static void flushReport() {

        if (extent != null) {
            extent.flush();
            System.out.println("Extent Report Generated Successfully");
        }
    }
}
