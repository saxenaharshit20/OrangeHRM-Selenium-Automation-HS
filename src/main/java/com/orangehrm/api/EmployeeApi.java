package com.orangehrm.api;

import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Cookie;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * REST API client for the OrangeHRM PIM employee endpoints observed in the
 * browser Network tab.
 *
 * The browser session is reused by forwarding Selenium cookies to REST Assured.
 * This avoids hard-coding credentials/tokens in the test project.
 */
public class EmployeeApi {

    private final String apiBaseUrl;
    private final String employeeCollectionPath;
    private final WebDriver driver;

    public EmployeeApi(WebDriver driver) {
        this.driver = driver;
        this.apiBaseUrl = com.orangehrm.utils.ConfigReader.get("apiBaseUrl");
        this.employeeCollectionPath = com.orangehrm.utils.ConfigReader.get("apiEmployeeCollectionPath");

        if (apiBaseUrl == null || apiBaseUrl.isBlank()) {
            throw new IllegalStateException("apiBaseUrl is not configured.");
        }
        if (employeeCollectionPath == null || employeeCollectionPath.isBlank()) {
            throw new IllegalStateException("apiEmployeeCollectionPath is not configured.");
        }
    }

    private io.restassured.specification.RequestSpecification authenticatedRequest() {
        var request = given()
                .baseUri(apiBaseUrl)
                .accept("application/json, text/plain, */*")
                .header("X-Requested-With", "XMLHttpRequest");

        for (Cookie cookie : driver.manage().getCookies()) {
            request.cookie(cookie.getName(), cookie.getValue());
        }

        return request;
    }

    /** GET /api/v2/pim/employees?limit=50&offset=0 */
    public Response getEmployees() {
        return authenticatedRequest()
                .queryParam("limit", 50)
                .queryParam("offset", 0)
                .when()
                .get(employeeCollectionPath)
                .then()
                .extract()
                .response();
    }

    /** GET /api/v2/pim/employees/{empNumber} */
    public Response getEmployeeByNumber(int empNumber) {
        return authenticatedRequest()
                .when()
                .get(employeeCollectionPath + "/" + empNumber)
                .then()
                .extract()
                .response();
    }

    /** DELETE /api/v2/pim/employees with body {"ids":[empNumber]}. */
    public Response deleteEmployeeByNumber(int empNumber) {
        return authenticatedRequest()
                .contentType("application/json")
                .body(Map.of("ids", List.of(empNumber)))
                .when()
                .delete(employeeCollectionPath)
                .then()
                .extract()
                .response();
    }

    /**
     * Finds the internal empNumber using the employeeId shown in the UI.
     */
    public int findEmpNumberByEmployeeId(String employeeId) {
        Response response = getEmployees();
        response.then().statusCode(200);

        List<Map<String, Object>> data = response.jsonPath().getList("data");
        if (data == null) {
            throw new AssertionError("Employee API response does not contain a data array: " + response.asString());
        }

        return data.stream()
                .filter(row -> employeeId.equals(String.valueOf(row.get("employeeId"))))
                .findFirst()
                .map(row -> Integer.parseInt(String.valueOf(row.get("empNumber"))))
                .orElseThrow(() -> new AssertionError(
                        "Employee ID not found through API: " + employeeId));
    }
}
