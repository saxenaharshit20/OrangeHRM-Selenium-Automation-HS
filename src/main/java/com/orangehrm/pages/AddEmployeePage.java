package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AddEmployeePage extends BasePage {

    private final By firstNameInput =
            By.xpath("(//input[contains(@placeholder,'First Name')])[1]");

    private final By lastNameInput =
            By.xpath("(//input[contains(@placeholder,'Last Name')])[1]");

    private final By employeeIdInput =
            By.xpath("//label[normalize-space()='Employee Id']/following::input[1]");

    private final By profilePictureInput =
            By.cssSelector("input[type='file'].oxd-file-input");

    private final By saveButton =
            By.xpath("//button[normalize-space()='Save']");

    public AddEmployeePage(WebDriver driver) {
        super(driver);
    }

    public void enterFirstName(String value) {
        enterText(firstNameInput, value);
    }

    public void enterLastName(String value) {
        enterText(lastNameInput, value);
    }

    public void enterEmployeeId(String value) {

        WebElement employeeIdField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(employeeIdInput)
        );

        /*
         * Do not click the field.
         * OrangeHRM can intercept the click because of its
         * input/overlay behavior.
         *
         * CTRL+A directly on the WebElement selects the
         * existing Employee ID.
         */
        employeeIdField.sendKeys(Keys.CONTROL, "a");

        employeeIdField.sendKeys(Keys.BACK_SPACE);

        employeeIdField.sendKeys(value);

        System.out.println("Employee ID entered: " + value);
    }

    public void uploadProfilePicture(String path) {
        uploadFile(profilePictureInput, path);
    }

    public void clickSave() {

        WebElement save = wait.until(
                ExpectedConditions.presenceOfElementLocated(saveButton)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                save
        );

        System.out.println("Clicking Save button...");

        try {

            wait.until(
                    ExpectedConditions.elementToBeClickable(saveButton)
            );

            save.click();

        } catch (Exception e) {

            System.out.println(
                    "Normal Save click failed. Using JavaScript click."
            );

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    save
            );
        }

        /*
         * Wait for OrangeHRM to leave the Add Employee page.
         */
        wait.until(driver ->
                !driver.getCurrentUrl().contains("/pim/addEmployee")
        );

        System.out.println(
                "Employee Save completed. Current URL: "
                        + driver.getCurrentUrl()
        );
    }

    public void addEmployee(
            String firstName,
            String lastName,
            String employeeId,
            String profilePicturePath) {

        enterFirstName(firstName);

        enterLastName(lastName);

        enterEmployeeId(employeeId);

        uploadProfilePicture(profilePicturePath);

        clickSave();
    }
}