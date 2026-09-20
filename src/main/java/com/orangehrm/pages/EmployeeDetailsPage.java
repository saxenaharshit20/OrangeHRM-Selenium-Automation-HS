package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EmployeeDetailsPage extends BasePage {

    private final By middleNameInput =
            By.cssSelector("input[placeholder='Middle Name']");

    private final By saveButton =
            By.xpath("//button[normalize-space()='Save']");

    public EmployeeDetailsPage(WebDriver driver) {
        super(driver);
    }

    public void updatePersonalDetails(String middleName) {
        enterText(middleNameInput, middleName);

        scrollIntoView(saveButton);
        click(saveButton);
    }
}