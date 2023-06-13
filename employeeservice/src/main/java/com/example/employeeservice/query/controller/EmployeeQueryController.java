package com.example.employeeservice.query.controller;

import com.example.employeeservice.query.model.EmployeeResponseModel;
import com.example.employeeservice.query.query.GetAllEmployeeQuery;
import com.example.employeeservice.query.query.GetEmployeesQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeQueryController {
    private final QueryGateway queryGateway;
    @Autowired
    public EmployeeQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/{employeeId}")
    public EmployeeResponseModel getEmployeeDetail(@PathVariable String employeeId) {
        GetEmployeesQuery getEmployeesQuery = new GetEmployeesQuery();
        getEmployeesQuery.setEmployeeId(employeeId);

        return queryGateway.query(getEmployeesQuery,
                        ResponseTypes.instanceOf(EmployeeResponseModel.class))
                .join();
    }
    @GetMapping
    public List<EmployeeResponseModel> getAllEmployee(){
        return queryGateway.query(new GetAllEmployeeQuery(), ResponseTypes.multipleInstancesOf(EmployeeResponseModel.class))
                .join();
    }
}
