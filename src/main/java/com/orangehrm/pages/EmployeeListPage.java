package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class EmployeeListPage extends BasePage {

    private final By employeeIdInput =
            By.xpath("//label[normalize-space()='Employee Id']/following::input[1]");

    private final By searchButton =
            By.xpath("//button[normalize-space()='Search']");

    private final By resetButton =
            By.xpath("//button[normalize-space()='Reset']");

    private final By deleteConfirmationButton =
            By.xpath("//button[normalize-space()='Yes, Delete']");

    public EmployeeListPage(WebDriver driver) {
        super(driver);
    }

    public void searchByEmployeeId(String employeeId) {

        enterText(employeeIdInput, employeeId);

        click(searchButton);

        /*
         * Do NOT wait for the employee row here.
         *
         * The row may not exist, and that should be handled
         * by isEmployeePresent().
         *
         * We only wait for the search operation/table to update.
         */
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void resetSearch() {
        click(resetButton);
    }

    private By employeeRow(String employeeId) {
        return By.xpath(
                "//div[contains(@class,'oxd-table-row')]" +
                "[.//div[contains(@class,'oxd-table-cell') " +
                "and normalize-space()='" + employeeId + "']]"
        );
    }

    public boolean isEmployeePresent(String employeeId) {

        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            employeeRow(employeeId)
                    )
            ).isDisplayed();

        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clickEditForEmployee(String employeeId) {

        By editButton = By.xpath(
                "//div[contains(@class,'oxd-table-row')]" +
                "[.//div[contains(@class,'oxd-table-cell') " +
                "and normalize-space()='" + employeeId + "']]" +
                "//button[contains(@class,'oxd-icon-button')][1]"
        );

        click(editButton);
    }

    public void clickDeleteForEmployee(String employeeId) {

        By deleteButton = By.xpath(
                "//div[contains(@class,'oxd-table-row')]" +
                "[.//div[contains(@class,'oxd-table-cell') " +
                "and normalize-space()='" + employeeId + "']]" +
                "//button[contains(@class,'oxd-icon-button')][2]"
        );

        click(deleteButton);
    }

    public void confirmDelete() {
        click(deleteConfirmationButton);
    }

    public void waitUntilEmployeeIsRemoved(String employeeId) {
        waitForInvisibility(employeeRow(employeeId));
    }
}