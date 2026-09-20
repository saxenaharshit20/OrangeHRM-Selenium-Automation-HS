package com.orangehrm.base;

import com.orangehrm.utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(
                        Long.parseLong(ConfigReader.get("explicitWait"))
                )
        );
    }

    protected void click(By locator) {

        WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        );

        scrollIntoView(locator);

        element.click();
    }

    protected void enterText(By locator, String text) {

        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );

        scrollIntoView(locator);

        element.click();
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        ).getText();
    }

    protected boolean isDisplayed(By locator) {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(locator)
            ).isDisplayed();

        } catch (TimeoutException e) {

            return false;
        }
    }

    protected void uploadFile(By locator, String filePath) {

        File file = new File(filePath);

        if (!file.exists()) {
            throw new RuntimeException(
                    "File not found: " + file.getAbsolutePath()
            );
        }

        WebElement element = wait.until(
                ExpectedConditions.presenceOfElementLocated(locator)
        );

        element.sendKeys(file.getAbsolutePath());
    }

    protected void scrollIntoView(By locator) {

        WebElement element = wait.until(
                ExpectedConditions.presenceOfElementLocated(locator)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                element
        );
    }

    protected void selectCustomDropdownOption(
            By dropdown,
            String optionText) {

        click(dropdown);

        By option = By.xpath(
                "//div[@role='option']" +
                "[normalize-space()='" + optionText + "']"
        );

        click(option);
    }

    protected void waitForUrlContains(String value) {

        wait.until(
                ExpectedConditions.urlContains(value)
        );
    }

    protected void waitForInvisibility(By locator) {

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(locator)
        );
    }
}