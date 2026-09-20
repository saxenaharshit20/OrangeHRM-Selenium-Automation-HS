package com.orangehrm.tests;

import com.orangehrm.api.EmployeeApi;
import com.orangehrm.base.BaseTest;
import com.orangehrm.models.EmployeeData;
import com.orangehrm.pages.AddEmployeePage;
import com.orangehrm.pages.EmployeeDetailsPage;
import com.orangehrm.pages.EmployeeListPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.PIMPage;
import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.JsonDataReader;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * OrangeHRM Employee Lifecycle Management
 *
 * Scenario 1 - Login
 * Scenario 2 - Add Employee
 * Scenario 3 - Edit Employee
 * Scenario 4 - API Validation
 * Scenario 5 - Delete Employee
 * Scenario 6 - Logout
 */
public class EmployeeLifecycleTest extends BaseTest {

    private EmployeeData employee;

    private LoginPage loginPage;
    private PIMPage pimPage;
    private EmployeeListPage employeeListPage;
    private EmployeeDetailsPage detailsPage;
    private EmployeeApi employeeApi;

    private int empNumber;


    @BeforeClass(alwaysRun = true)
    public void initializeTestData() {

        employee = JsonDataReader.getEmployeeData();

        /*
         * Generate a unique Employee ID.
         *
         * OrangeHRM allows a maximum of 10 characters
         * for Employee ID.
         *
         * Example:
         * E12345678
         *
         * Total = 9 characters.
         */
        String uniqueId =
                "E" + (System.currentTimeMillis() % 100000000);

        employee.setEmployeeId(uniqueId);

        System.out.println(
                "Generated Employee ID: " + uniqueId
        );

        loginPage = new LoginPage(driver);

        pimPage = new PIMPage(driver);

        employeeListPage =
                new EmployeeListPage(driver);

        detailsPage =
                new EmployeeDetailsPage(driver);

        employeeApi =
                new EmployeeApi(driver);
    }


    // =========================================================
    // SCENARIO 1 - LOGIN
    // =========================================================

    @Test(
            priority = 1,
            description = "Scenario 1 - Login"
    )
    public void scenario1_Login() {

        loginPage.login(
                ConfigReader.get("username"),
                ConfigReader.get("password")
        );

        Assert.assertTrue(
                loginPage.isDashboardDisplayed(),
                "Dashboard should be displayed after successful login"
        );
    }


    // =========================================================
    // SCENARIO 2 - ADD EMPLOYEE
    // =========================================================

    @Test(
            priority = 2,
            dependsOnMethods = "scenario1_Login",
            description = "Scenario 2 - Add Employee"
    )
    public void scenario2_AddEmployee() {

        pimPage.clickPIM();

        pimPage.clickAddEmployee();

        AddEmployeePage addEmployeePage =
                new AddEmployeePage(driver);

        addEmployeePage.addEmployee(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmployeeId(),
                employee.getProfilePicture()
        );

        /*
         * Success toast verification intentionally omitted.
         *
         * Employee presence is verified in Scenario 3.
         */
    }


    // =========================================================
    // SCENARIO 3 - EDIT EMPLOYEE
    // =========================================================

    @Test(
            priority = 3,
            dependsOnMethods = "scenario2_AddEmployee",
            description = "Scenario 3 - Edit Employee"
    )
    public void scenario3_EditEmployee() {

        pimPage.clickEmployeeList();

        employeeListPage.searchByEmployeeId(
                employee.getEmployeeId()
        );

        Assert.assertTrue(
                employeeListPage.isEmployeePresent(
                        employee.getEmployeeId()
                ),
                "Newly created employee should be present in Employee List"
        );

        employeeListPage.clickEditForEmployee(
                employee.getEmployeeId()
        );

        /*
         * Edit Personal Details only.
         * Job Title / Employment Status dropdowns are
         * intentionally not used.
         */
        detailsPage.updatePersonalDetails("Updated");

        /*
         * Verify employee is still present after editing.
         */
        pimPage.clickEmployeeList();

        employeeListPage.searchByEmployeeId(
                employee.getEmployeeId()
        );

        Assert.assertTrue(
                employeeListPage.isEmployeePresent(
                        employee.getEmployeeId()
                ),
                "Employee should still be present after editing Personal Details"
        );
    }


    // =========================================================
    // SCENARIO 4 - API VALIDATION
    // =========================================================

    @Test(
            priority = 4,
            dependsOnMethods = "scenario3_EditEmployee",
            description = "Scenario 4 - API Validation"
    )
    public void scenario4_ApiValidation() {

        empNumber =
                employeeApi.findEmpNumberByEmployeeId(
                        employee.getEmployeeId()
                );

        Response response =
                employeeApi.getEmployeeByNumber(
                        empNumber
                );

        Assert.assertEquals(
                response.statusCode(),
                200,
                "Employee GET API should return 200"
        );

        Assert.assertEquals(
                response.jsonPath().getString(
                        "data.employeeId"
                ),
                employee.getEmployeeId(),
                "API employeeId should match the UI-created employee"
        );

        Assert.assertEquals(
                response.jsonPath().getString(
                        "data.firstName"
                ),
                employee.getFirstName(),
                "API firstName should match the UI-created employee"
        );

        Assert.assertEquals(
                response.jsonPath().getString(
                        "data.lastName"
                ),
                employee.getLastName(),
                "API lastName should match the UI-created employee"
        );
    }


    // =========================================================
    // SCENARIO 5 - DELETE EMPLOYEE
    // =========================================================

    @Test(
            priority = 5,
            dependsOnMethods = "scenario4_ApiValidation",
            description = "Scenario 5 - Delete Employee and API Verification"
    )
    public void scenario5_DeleteEmployee() {

        pimPage.clickEmployeeList();

        employeeListPage.searchByEmployeeId(
                employee.getEmployeeId()
        );

        Assert.assertTrue(
                employeeListPage.isEmployeePresent(
                        employee.getEmployeeId()
                ),
                "Employee should exist before deletion"
        );

        employeeListPage.clickDeleteForEmployee(
                employee.getEmployeeId()
        );

        employeeListPage.confirmDelete();

        employeeListPage.waitUntilEmployeeIsRemoved(
                employee.getEmployeeId()
        );

        Assert.assertFalse(
                employeeListPage.isEmployeePresent(
                        employee.getEmployeeId()
                ),
                "Employee should be removed from Employee List"
        );

        Response response =
                employeeApi.getEmployeeByNumber(
                        empNumber
                );

        /*
         * OrangeHRM demo environment returns 422
         * for the deleted employee GET request.
         */
        Assert.assertEquals(
                response.statusCode(),
                422,
                "API should return 422 after employee deletion"
        );
    }


    // =========================================================
    // SCENARIO 6 - LOGOUT
    // =========================================================

    @Test(
            priority = 6,
            dependsOnMethods = "scenario5_DeleteEmployee",
            description = "Scenario 6 - Logout and Session Validation"
    )
    public void scenario6_Logout() {

        loginPage.logout();

        Assert.assertTrue(
                loginPage.isLoginPageDisplayed(),
                "Login page should be displayed after logout"
        );
    }
}