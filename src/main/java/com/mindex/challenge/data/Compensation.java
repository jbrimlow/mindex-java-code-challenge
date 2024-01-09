package com.mindex.challenge.data;

import java.time.LocalDate;

public class Compensation {
    // We could also embed an Employee object in the Compensation object, but that would mean we'd have duplicate
    // Employee data, so we'd have to keep it synced. We also might expect Employee to change more often than
    // Compensation, since changes to the list of direct reports of a manager or an employee's department would
    // probably not result in a change in compensation.
    private String employeeId;
    private double salary;

    // This assumes that compensation changes are effective on a given date, which seems reasonable. We could extend
    // this to take account of time of day in addition to the date if desired, but that doesn't seem like a standard
    // salary requirement.
    private LocalDate effectiveDate;

    public Compensation() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
