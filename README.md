# OrangeHRM Automation Framework

A Selenium test automation framework for the OrangeHRM demo application, built with Java, TestNG and Maven using the Page Object Model. It combines UI testing, Excel data-driven testing and API validation.

## Tech Stack
- Java 17
- Selenium WebDriver
- TestNG
- Maven
- Apache POI (Excel test data)
- REST Assured (API validation)
- Extent Reports (HTML reports with failure screenshots)
- WebDriverManager

## What It Tests
- **Login**: valid and invalid login, plus Excel data-driven login (valid user, wrong password, wrong user, blank user)
- **PIM (employee management)**: add, search, edit and delete an employee
- **Excel vs UI validation**: employee data from one Excel file is created in the application, then the values shown in the UI are compared with a second "expected" Excel file
- **API vs UI validation**: an employee created in the UI is fetched through the OrangeHRM REST API, and the API response is compared with the UI

## Framework Design
- **Page Object Model**: locators and page actions live in page classes, and tests only call methods and assert
- **Data-driven**: TestNG `@DataProvider` reads test data from Excel through Apache POI
- **Unique test data**: employee names and IDs get a timestamp so tests do not collide on the shared demo site
- **Stable waits**: explicit, condition-based waits (including longer waits for slow server steps) with readable failure messages
- **Reporting**: a TestNG listener writes results to an Extent Report and attaches a screenshot on failure

## Project Structure
```
src/main/java/com/orangehrmproj
    base/        BaseClass (browser setup and teardown)
    pages/       LoginPage, DashboardPage, PIMPage
    reports/     ExtentReportManager, TestListener
    utilities/   ExcelUtils, ScreenshotUtils
src/test/java/com/orangehrmproj/tests
    LoginTest, LoginDataTest, PIMTest, PIMCrudTest, ApiVsUiTest
testdata/        LoginData.xlsx, EmployeeData.xlsx, ExpectedEmployeeData.xlsx
testng.xml       Suite definition
pom.xml          Dependencies and build
```

## How to Run
Prerequisites: JDK 17 or higher, Maven, Google Chrome.

```
mvn test
```

This runs the suite defined in `testng.xml`. The full suite takes several minutes because the public demo site can be slow.

After a run, open `reports/OrangeHRMReport.html` for the report. Failure screenshots are saved in `screenshots/`.

## Notes
- The tests run against the public OrangeHRM demo site (`opensource-demo.orangehrmlive.com`), which is shared and can be slow or reset its data.
- Generated folders (`target`, `reports`, `screenshots`, `test-output`) are not committed.

## Planned
- Jenkins job to run the suite automatically
- Negative tests (blank first name, unknown employee)
- Externalised configuration (`config.properties`) instead of hardcoded login details

## Author
Kiruthika