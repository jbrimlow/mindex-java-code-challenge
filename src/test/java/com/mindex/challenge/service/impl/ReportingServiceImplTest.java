package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingServiceImplTest {

    private String reportingStructureUrl;

    // ids for testing using repository data
    private String johnLennonId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
    private String paulMcCartneyId = "b7839309-3348-463b-a7e3-5de1c168beb3";
    private String badId = "I am not in the database";

    // repository to test reporting structure using provided employee_database.json file
    @Autowired
    private EmployeeRepository employeeRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureUrl = "http://localhost:" + port + "/employee/{id}/reporting-structure";
    }

    @Test
    public void testNumberOfReports() {
        // test with someone who has more than zero reports (4, the example from the README)
        ReportingStructure lennonReportingStruct = restTemplate.getForEntity(
                reportingStructureUrl,
                ReportingStructure.class,
                johnLennonId
        ).getBody();
        assertNotNull(lennonReportingStruct);
        assertEquals(lennonReportingStruct.getNumberOfReports(), 4);

        // test with someone who has zero reports
        ReportingStructure mcCartneyReportingStruct = restTemplate.getForEntity(
                reportingStructureUrl,
                ReportingStructure.class,
                paulMcCartneyId
        ).getBody();
        assertNotNull(mcCartneyReportingStruct);
        assertEquals(mcCartneyReportingStruct.getNumberOfReports(), 0);
    }

    // test reporting structure code throws correct exception if unknown id supplied
    @Test
    public void testNumberOfReportsThrowsRuntimeException(){
        try {
            restTemplate.getForEntity(
                    reportingStructureUrl,
                    ReportingStructure.class,
                    badId
            ).getBody();
        } catch (RuntimeException exception) {
            // we have the right exception class, confirm message is right
            assertEquals(exception.getMessage(), "Invalid employeeId: " + badId);
        }
    }
}
