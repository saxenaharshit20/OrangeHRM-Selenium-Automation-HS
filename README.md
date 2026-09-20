# OrangeHRM Selenium + TestNG Automation

Maven/Eclipse-ready QA automation assignment using Selenium WebDriver, Java 17, TestNG, Page Object Model, JSON test data, REST Assured, ExtentReports and desktop video recording.

## Test scenarios

1. Login
2. Add Employee, including profile picture upload
3. Edit Employee Job details
4. API validation of the created/edited employee
5. Delete Employee through UI + API verification
6. Logout/session invalidation

## API used

The employee API was identified from the OrangeHRM browser Network traffic:

- GET collection: `/web/index.php/api/v2/pim/employees?limit=50&offset=0`
- GET one employee: `/web/index.php/api/v2/pim/employees/{empNumber}`
- DELETE employees: `/web/index.php/api/v2/pim/employees` with JSON body `{ "ids": [empNumber] }`

The REST Assured client reuses the authenticated Selenium browser cookies rather than storing an API token in the project.

## Run

1. Import as **Maven > Existing Maven Projects**.
2. Use Java 17.
3. Maven > Update Project.
4. Right-click `testng.xml` > **Run As > TestNG Suite**.

Or from a terminal:

```bash
mvn clean test
```

## Output

- Extent HTML report: `reports/ExtentReport.html`
- Failure screenshots: `screenshots/`
- Desktop recordings: `videos/`

## Notes

The OrangeHRM public demo is shared. The test creates one employee from `src/test/resources/testdata/employee.json` and deletes it as part of the lifecycle test.

The API client is based on the employee endpoints observed in DevTools Network and the documented OrangeHRM v2 employee/delete contract.


## TestNG scenarios
The suite exposes exactly six TestNG test methods: Scenario 1 Login, Scenario 2 Add Employee, Scenario 3 Edit Employee, Scenario 4 API Validation, Scenario 5 Delete Employee + API verification, and Scenario 6 Logout/session validation. They run in order and share the same browser session.
