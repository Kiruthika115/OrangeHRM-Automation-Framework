package com.orangehrmproj.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PIMPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By pimMenu = By.xpath("//span[text()='PIM']");
    private By addEmployeeTab = By.xpath("//a[text()='Add Employee']");
    private By employeeListTab = By.xpath("//a[text()='Employee List']");
    private By firstNameField = By.name("firstName");
    private By lastNameField = By.name("lastName");
    private By employeeIdField = By.xpath("//label[text()='Employee Id']/parent::div/following-sibling::div//input");
    private By submitButton = By.xpath("//button[@type='submit']");
    private By formLoader = By.xpath("//div[contains(@class,'oxd-form-loader')]");
    private By personalDetailsHeader = By.xpath("//h6[text()='Personal Details']");
    private By fieldError = By.xpath("//span[contains(@class,'oxd-input-field-error-message')]");
    private By nameSearchField = By.xpath("(//input[@placeholder='Type for hints...'])[1]");
    private By resultRows = By.xpath("//div[@class='oxd-table-body']//div[@role='row']");
    private By firstRowFirstName = By.xpath("(//div[@class='oxd-table-body']//div[@role='row'])[1]/div[3]");
    private By firstRowLastName = By.xpath("(//div[@class='oxd-table-body']//div[@role='row'])[1]/div[4]");
    private By editIcon = By.xpath("(//div[@class='oxd-table-body']//div[@role='row'])[1]//i[contains(@class,'bi-pencil-fill')]");
    private By deleteIcon = By.xpath("(//div[@class='oxd-table-body']//div[@role='row'])[1]//i[contains(@class,'bi-trash')]");
    private By confirmDelete = By.xpath("//button[normalize-space()='Yes, Delete']");
    private By toast = By.xpath("//div[contains(@class,'oxd-toast')]");
    private By noRecords = By.xpath("//span[text()='No Records Found']");

    public PIMPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // longer wait for steps that depend on the (slow) demo server,
    // with a readable message if it still times out
    private FluentWait<WebDriver> slowWait(String message) {
        return new WebDriverWait(driver, Duration.ofSeconds(45)).withMessage(message);
    }

    // waits for the loading overlay to go away, then clicks Save/Search
    private void clickSubmit() {
        slowWait("Loading overlay did not disappear within 45s")
                .until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public void openPIM() {
        wait.until(ExpectedConditions.elementToBeClickable(pimMenu)).click();
    }

    public void addEmployee(String firstName, String lastName) {
        wait.until(ExpectedConditions.elementToBeClickable(addEmployeeTab)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);

        // the demo site's auto-generated Employee Id can already exist,
        // so wait for it to load, then replace it with our own unique Id
        wait.until(ExpectedConditions.visibilityOfElementLocated(employeeIdField));
        wait.until(d -> !d.findElement(employeeIdField).getAttribute("value").isEmpty());
        String uniqueId = String.valueOf(System.currentTimeMillis()).substring(5);
        driver.findElement(employeeIdField).sendKeys(Keys.chord(Keys.CONTROL, "a"), uniqueId);

        clickSubmit();

        // wait for either the success page or a form error
        slowWait("Employee save did not finish within 45s (demo site slow?)")
                .until(d -> !d.findElements(personalDetailsHeader).isEmpty()
                         || !d.findElements(fieldError).isEmpty());

        if (!driver.findElements(fieldError).isEmpty()) {
            throw new AssertionError("Employee was not saved. Form error: "
                    + driver.findElement(fieldError).getText());
        }
    }

    public void searchEmployee(String employeeName) {
        wait.until(ExpectedConditions.elementToBeClickable(employeeListTab)).click();

        WebElement nameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(nameSearchField));
        nameField.clear();
        nameField.sendKeys(employeeName);

        clickSubmit();

        // wait until the table has filtered down to exactly one record
        slowWait("Search did not return exactly one record within 45s")
                .until(d -> d.findElements(resultRows).size() == 1);
    }

    public void searchEmployeeExpectNone(String employeeName) {
        wait.until(ExpectedConditions.elementToBeClickable(employeeListTab)).click();
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(nameSearchField));
        nameField.clear();
        nameField.sendKeys(employeeName);
        clickSubmit();
        slowWait("'No Records Found' did not appear within 45s")
                .until(ExpectedConditions.visibilityOfElementLocated(noRecords));
    }

    public void editLastName(String newLastName) {
        wait.until(ExpectedConditions.elementToBeClickable(editIcon)).click();
        slowWait("Edit page did not open within 45s")
                .until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader));

        // the form loads data after it appears, so wait until the field is filled
        slowWait("Employee data did not load on the edit page within 45s")
                .until(d -> !d.findElement(lastNameField).getAttribute("value").isEmpty());

        WebElement field = driver.findElement(lastNameField);
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"), newLastName);

        clickSubmit();
        slowWait("Update confirmation did not appear within 45s")
                .until(ExpectedConditions.visibilityOfElementLocated(toast));
    }

    public void deleteFirstResult() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteIcon)).click();
        wait.until(ExpectedConditions.elementToBeClickable(confirmDelete)).click();
        slowWait("Delete confirmation did not appear within 45s")
                .until(ExpectedConditions.visibilityOfElementLocated(toast));
    }

    public boolean isNoRecordsFound() {
        return driver.findElements(noRecords).size() > 0;
    }

    public int getResultCount() {
        return driver.findElements(resultRows).size();
    }

    public String getFirstResultFirstName() {
        return driver.findElement(firstRowFirstName).getText().trim();
    }

    public String getFirstResultLastName() {
        return driver.findElement(firstRowLastName).getText().trim();
    }
}