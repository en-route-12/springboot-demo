package com.example.demo.controller;

import com.example.demo.entity.Batch;
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

    @GetMapping("/batches/{course}")
    public List<MasterService.BatchCardsDTO> getBatchesByCourse(@PathVariable String course){
        return masterService.getBatchesByCourse(course);
    }

    @GetMapping("/form")
    public MasterService.BuildBatchDTO getAvailableCoursesAndInstructors() {
        return masterService.getAvailableCoursesAndInstructors();
    }

    @PostMapping("/save")
    public Batch saveBatch(@RequestBody MasterService.BatchFormDTO batchFormDTO) {
        return masterService.saveBatch(batchFormDTO);
    }

    @GetMapping("batch/{batchId}")
    public MasterService.BatchInfoDTO getBatchInfoByBatchId(@PathVariable int batchId){
        return masterService.getBatchInfoByBatchId(batchId);
    }

    @GetMapping("/students/{batchId}")
    public List<MasterService.StudentPaymentDetailDTO> getStudentDetails(@PathVariable int batchId){
        return masterService.getStudentDetails(batchId);
    }
}
