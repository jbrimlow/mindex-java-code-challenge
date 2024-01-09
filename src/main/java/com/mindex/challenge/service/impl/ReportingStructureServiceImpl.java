package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Reading reporting structure for employee with id [{}]", id);

        Employee employee = employeeService.read(id);

        ReportingStructure reportingStructure = new ReportingStructure();

        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(getNumberOfReports(employee));

        return reportingStructure;
    }

    private int getNumberOfReports(Employee employee) {
        int numberOfReports = 0;
        if (employee == null || employee.getDirectReports() == null || employee.getDirectReports().isEmpty()) {
            return numberOfReports;
        } else {
            numberOfReports += employee.getDirectReports().size();
            for (Employee report: employee.getDirectReports()) {
                numberOfReports += getNumberOfReports(employeeService.read(report.getEmployeeId()));
            }
        }
        return numberOfReports;
    }
}
