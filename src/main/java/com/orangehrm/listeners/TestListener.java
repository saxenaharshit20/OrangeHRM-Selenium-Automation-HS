package com.orangehrm.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.orangehrm.base.DriverFactory;
import com.orangehrm.constants.FrameworkConstants;
import com.orangehrm.utils.ScreenshotUtils;
import org.testng.*;

import java.nio.file.Files;
import java.nio.file.Path;

public class TestListener implements ITestListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    private static synchronized ExtentReports getExtent() {
        if (extent == null) {
            try {
                Files.createDirectories(Path.of("reports"));
            } catch (Exception e) {
                throw new RuntimeException("Unable to create reports directory", e);
            }

            ExtentSparkReporter reporter =
                    new ExtentSparkReporter(FrameworkConstants.REPORT_PATH);

            reporter.config().setReportName("OrangeHRM Automation");
            reporter.config().setDocumentTitle("OrangeHRM Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }

        return extent;
    }

    @Override
    public void onTestStart(ITestResult result) {
        TEST.set(
                getExtent().createTest(
                        result.getTestClass().getName()
                                + " :: "
                                + result.getMethod().getMethodName()
                )
        );
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        TEST.get().pass("Test passed");
        remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = TEST.get();
        test.fail(result.getThrowable());

        if (DriverFactory.getDriver() != null) {
            try {
                String path = ScreenshotUtils.capture(
                        DriverFactory.getDriver(),
                        result.getMethod().getMethodName() + "_failure"
                );

                test.addScreenCaptureFromPath(path);
            } catch (Exception e) {
                test.warning("Unable to attach screenshot: " + e.getMessage());
            }
        }

        remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        TEST.get().skip(result.getThrowable());
        remove();
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }

    private void remove() {
        TEST.remove();
    }
}
