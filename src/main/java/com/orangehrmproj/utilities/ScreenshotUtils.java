
package com.orangehrmproj.utilities;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtils {

    public static String captureScreenshot(
            WebDriver driver, String testName) {

        File source = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);

        String path = System.getProperty("user.dir")
                + "/screenshots/"
                + testName + ".png";

        File destination = new File(path);

        try {
            FileUtils.copyFile(source, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }
}
