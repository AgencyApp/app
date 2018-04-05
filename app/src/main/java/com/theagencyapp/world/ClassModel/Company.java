package com.theagencyapp.world.ClassModel;

/**
 * Created by hamza on 05-Apr-18.
 */

public class Company {
    String companyName;
    String logoUrl;
    String address;
    String companyType;
    int clientCode;
    int employeeCode;

    public Company(String companyName, String logoUrl, String address, String companyType, int clientCode, int employeeCode) {
        this.companyName = companyName;
        this.logoUrl = logoUrl;
        this.address = address;
        this.companyType = companyType;
        this.clientCode = clientCode;
        this.employeeCode = employeeCode;
    }

    public Company() {
    }

    public int getClientCode() {
        return clientCode;
    }

    public void setClientCode(int clientCode) {
        this.clientCode = clientCode;
    }

    public int getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(int employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }
}
