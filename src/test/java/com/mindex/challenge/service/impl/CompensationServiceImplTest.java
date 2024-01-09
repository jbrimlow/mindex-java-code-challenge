package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;
    private String testId = "id-123";
    private String badId = "I am not in the database";

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateRead() {
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId(testId);
        testCompensation.setSalary(123456.78);
        testCompensation.setEffectiveDate(LocalDate.parse("2023-01-01"));

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();

        assertCompensationEquivalence(testCompensation, createdCompensation);

        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(
                compensationIdUrl,
                Compensation.class,
                createdCompensation.getEmployeeId()
        ).getBody();

        assertCompensationEquivalence(readCompensation, createdCompensation);
    }

    // test exception if we request compensation for id that's not in the database
    @Test
    public void testReadRuntimeException() {
        try {
            restTemplate.getForEntity(compensationIdUrl, Compensation.class, badId).getBody();
        } catch (RuntimeException exception) {
            // we have the right exception class, confirm message is right
            assertEquals(exception.getMessage(), "Invalid employeeId: " + badId);
        }
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary(), 0.001);
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
