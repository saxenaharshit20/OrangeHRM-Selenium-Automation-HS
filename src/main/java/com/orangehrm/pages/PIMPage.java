package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PIMPage extends BasePage {

    private final By pimMenu =
            By.xpath("//span[normalize-space()='PIM']");

    private final By employeeListTab =
            By.xpath("//a[normalize-space()='Employee List']");

    private final By addEmployeeTab =
            By.xpath("//a[normalize-space()='Add Employee']");

    public PIMPage(WebDriver driver) {
        super(driver);
    }

    public void clickPIM() {
        click(pimMenu);
    }

    public void clickEmployeeList() {
        click(employeeListTab);
    }

    public void clickAddEmployee() {
        click(addEmployeeTab);
    }
}
