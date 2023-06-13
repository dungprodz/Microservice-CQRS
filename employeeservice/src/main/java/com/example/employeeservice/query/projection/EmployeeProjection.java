package com.example.employeeservice.query.projection;

import com.example.employeeservice.command.data.Employee;
import com.example.employeeservice.command.data.EmployeeRepository;
import com.example.employeeservice.query.model.EmployeeResponseModel;
import com.example.employeeservice.query.query.GetAllEmployeeQuery;
import com.example.employeeservice.query.query.GetEmployeesQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeProjection {
    private final EmployeeRepository employeeRepository;

    public EmployeeProjection(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    @QueryHandler
    public EmployeeResponseModel handle(GetEmployeesQuery getEmployeesQuery){
        Employee employee = employeeRepository.getById(getEmployeesQuery.getEmployeeId());
        EmployeeResponseModel employeeResponseModel=new EmployeeResponseModel();
        BeanUtils.copyProperties(employee, employeeResponseModel);
        return employeeResponseModel;
    }

    @QueryHandler
    public List<EmployeeResponseModel> handle(GetAllEmployeeQuery getAllEmployeeQuery){
        List<Employee> employee = employeeRepository.findAll();
        return employee.stream().map(e->{
            EmployeeResponseModel employeeResponseModel = new EmployeeResponseModel();
            employeeResponseModel.setEmployeeId(e.getEmployeeId());
            employeeResponseModel.setFirstName(e.getFirstName());
            employeeResponseModel.setLastName(e.getLastName());
            employeeResponseModel.setKin(e.getKin());
            employeeResponseModel.setIsDisciplined(e.getIsDisciplined());
            return employeeResponseModel;
        }).toList();
    }
}
