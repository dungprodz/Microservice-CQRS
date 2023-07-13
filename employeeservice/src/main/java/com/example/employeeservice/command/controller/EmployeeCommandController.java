package com.example.employeeservice.command.controller;

import com.example.employeeservice.command.command.CreateEmployeeCommand;
import com.example.employeeservice.command.command.DeleteEmployeeCommand;
import com.example.employeeservice.command.command.UpdateEmployeeCommand;
import com.example.employeeservice.command.model.EmployeeRequestModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@EnableBinding(Source.class)
public class EmployeeCommandController {

    private final CommandGateway commandGateway;

    private final MessageChannel output;

    @Autowired
    public EmployeeCommandController(CommandGateway commandGateway, MessageChannel output) {
        this.commandGateway = commandGateway;
        this.output = output;
    }

    @PostMapping
    public String addEmployee(@RequestBody EmployeeRequestModel model) {
        CreateEmployeeCommand command =
                new CreateEmployeeCommand(UUID.randomUUID().toString(), model.getFirstName(), model.getLastName(), model.getKin(), false);

        commandGateway.sendAndWait(command);

        return "emmployee added";
    }

    @PutMapping
    public String updateEmployee(@RequestBody EmployeeRequestModel model) {
        UpdateEmployeeCommand command =
                new UpdateEmployeeCommand(model.getEmployeeId(), model.getFirstName(), model.getLastName(), model.getKin(), model.getIsDisciplined());
        commandGateway.sendAndWait(command);
        return "employee updated";
    }

    @DeleteMapping("/{employeeId}")
    public String deleteEmployee(@PathVariable String employeeId) {
        DeleteEmployeeCommand command = new DeleteEmployeeCommand(employeeId);
        commandGateway.sendAndWait(command);
        return "emmployee deleted";
    }
    @PostMapping("/sendMessage")
    public void SendMessage(@RequestBody String message) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(message);
            output.send(MessageBuilder.withPayload(json).build());
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
