package com.example.demo.controller;

import com.example.demo.entity.UserRegistration;
import com.example.demo.service.MasterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/demo")
public class MasterController {
    private final MasterService masterService;

    public MasterController(MasterService masterService) {
        this.masterService = masterService;
    }

    @PostMapping
    public Optional<MasterService.LoginDTO> getLoginDetails(@RequestBody MasterService.LoginDTO loginDTO) {
        return masterService.getLoginDetails(loginDTO.getEmail(), loginDTO.getPassword());
    }

    @GetMapping("/course/{userId}")
    public List<MasterService.CourseDTO> getCoursesByUserId(@PathVariable int userId){
        return masterService.getCoursesByUserId(userId);
    }

    @PostMapping("/registration")
    public UserRegistration addUser(@RequestBody MasterService.UserRegistrationDTO userRegistrationDTO) {
        return masterService.addUser(userRegistrationDTO);
    }

    @PostMapping("/buildBatch")
    public MasterService.BuildBatchDTO buildBatchForm(@RequestBody MasterService.UserRegistrationDTO userRegistrationDTO) {
        return masterService.buildBatchForm(userRegistrationDTO);
    }

}
