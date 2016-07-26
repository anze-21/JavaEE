package com.kaishengit.pojo;

import java.util.Set;

public class Dept {
    private Integer id;
    private String deptname;
    private Set<Employee> employeesSet;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public Set<Employee> getEmployeesSet() {
        return employeesSet;
    }

    public void setEmployeesSet(Set<Employee> employeesSet) {
        this.employeesSet = employeesSet;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "id=" + id +
                ", deptname='" + deptname + '\'' +
                ", employeesSet=" + employeesSet +
                '}';
    }
}

