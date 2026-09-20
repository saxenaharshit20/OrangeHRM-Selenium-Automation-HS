package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By username =
            By.xpath("//input[@name='username']");

    private final By password =
            By.xpath("//input[@name='password']");

    private final By loginButton =
            By.xpath("//button[@type='submit']");

    private final By dashboardHeader =
            By.xpath("//h6[normalize-space()='Dashboard']");

    private final By userDropdown =
            By.cssSelector(".oxd-userdropdown-name");

    private final By logoutLink =
            By.xpath("//a[normalize-space()='Logout']");

    private final By loginHeader =
            By.xpath("//h5[normalize-space()='Login']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String usernameValue, String passwordValue) {
        enterText(username, usernameValue);
        enterText(password, passwordValue);
        click(loginButton);
    }

    public boolean isDashboardDisplayed() {
        return isDisplayed(dashboardHeader);
    }

    public void logout() {
        click(userDropdown);
        click(logoutLink);
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginHeader);
    }
}
