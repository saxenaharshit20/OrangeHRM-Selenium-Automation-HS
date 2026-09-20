package com.orangehrm.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orangehrm.constants.FrameworkConstants;
import com.orangehrm.models.EmployeeData;

import java.io.File;

public final class JsonDataReader {

    private JsonDataReader() {}

    public static EmployeeData getEmployeeData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(
                    new File(FrameworkConstants.EMPLOYEE_DATA_FILE),
                    EmployeeData.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Unable to read employee.json", e);
        }
    }
}
