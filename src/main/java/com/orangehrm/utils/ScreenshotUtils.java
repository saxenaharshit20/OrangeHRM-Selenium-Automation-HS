package com.orangehrm.utils;

import com.orangehrm.constants.FrameworkConstants;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class ScreenshotUtils {

    private ScreenshotUtils() {}

    public static String capture(WebDriver driver, String name) {

        try {
            Files.createDirectories(Path.of(FrameworkConstants.SCREENSHOT_PATH));

            File source = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            Path destination = Path.of(
                    FrameworkConstants.SCREENSHOT_PATH,
                    name + ".png"
            );

            Files.copy(
                    source.toPath(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return destination.toString();

        } catch (IOException e) {
            throw new RuntimeException("Unable to capture screenshot", e);
        }
    }
}
