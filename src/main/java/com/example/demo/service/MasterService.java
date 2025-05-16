package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.BatchRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LoginRepository;
import com.example.demo.repository.UserRegistrationRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MasterService {

    private final LoginRepository loginRepo;
    private final CourseRepository courseRepo;
    private final UserRegistrationRepository userRepo;
    private final BatchRepository batchRepo;

    public MasterService(LoginRepository loginRepo, CourseRepository courseRepo, UserRegistrationRepository userRepo, BatchRepository batchRepo) {
        this.loginRepo = loginRepo;
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
        this.batchRepo = batchRepo;
    }

    @Data
    public static class LoginDTO {
        private String email;
        private String password;
    }

    public Optional<LoginDTO> getLoginDetails(String email, String password) {
        if (email == null || password == null) {
            return Optional.empty();
        }
        return loginRepo.findById(email).map(this::convertEntityToDto);
    }

    public LoginDTO convertEntityToDto(Login login) {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(login.getEmail());
        loginDTO.setPassword(login.getPassword());
        return loginDTO;
    }

    @Data
    public static class UserRegistrationDTO {
        private int userId;
        private String fullName;
        private String email;
        private boolean isMaster;
    }

    public UserRegistration addUser(UserRegistrationDTO userRegistrationDTO) {
        UserRegistration userRegistration = convertDtoToUserEntity(userRegistrationDTO);
        return userRepo.save(userRegistration);
    }

    private UserRegistration convertDtoToUserEntity(UserRegistrationDTO userDto) {
        UserRegistration userRegistration1 = new UserRegistration();
        userRegistration1.setUserId(userDto.getUserId());
        userRegistration1.setEmail(userDto.getEmail());
        userRegistration1.setFullName(userDto.getFullName());
        userRegistration1.setMaster(userDto.isMaster());
        return userRegistration1;
    }

    @Data
    public static class CourseDTO {
        private String name;
        private URL icon;
        private String color;
        private int masterUserId;
    }

    public List<CourseDTO> getCoursesByUserId(int userId) {
        UserRegistration user = userRepo.findById(userId).orElse(null);
        return courseRepo.findByUser(user)
                .stream()
                .map(this::convertCourseEntityToDto)
                .collect(Collectors.toList());
    }

    private CourseDTO convertCourseEntityToDto(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName(course.getName());
        courseDTO.setIcon(course.getIcon());
        courseDTO.setColor(course.getColor());
        courseDTO.setMasterUserId(course.getUser().getUserId());
        return courseDTO;
    }

    @Data
    public static class BatchCardsDTO {
        private int batchId;
        private String batchName;
        private LocalTime startTime;
        private LocalTime endTime;
    }

    public List<BatchCardsDTO> getBatchesByCourse(String courseName) {
        Course course = courseRepo.findByName(courseName);
        return batchRepo.findByCourse(course)
                .stream()
                .map(this::getBatchCardsToEntity)
                .collect(Collectors.toList());
    }

    public BatchCardsDTO getBatchCardsToEntity(Batch batch){
        BatchCardsDTO batchCardsDTO = new BatchCardsDTO();
        batchCardsDTO.setBatchId(batch.getBatchId());
        batchCardsDTO.setBatchName(batch.getBatchName());
        batchCardsDTO.setStartTime(batch.getStartTime());
        batchCardsDTO.setEndTime(batch.getEndTime());
        return batchCardsDTO;
    }

    @Data
    @AllArgsConstructor
    public static class BatchFormDTO {
        private int batchId;
        private String batchName;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<String> days;
        private LocalTime startTime;
        private LocalTime endTime;
        private String location;
        private String locationAddress;
        private String courseName;
        private int instructorId;
    }

    @Data
    public static class BuildBatchDTO {
        private List<String> courses;
        private List<String> instructorNames;
        private BatchFormDTO batchFormDTO;

        public BuildBatchDTO(List<String> courses, List<String> instructorNames, BatchFormDTO batchFormDTO) {
            this.courses = courses;
            this.instructorNames = instructorNames;
            this.batchFormDTO = batchFormDTO;
        }
    }

    public BuildBatchDTO getAvailableCoursesAndInstructors() {
        List<String> courses = courseRepo.findAll()
                .stream()
                .map(Course::getName)
                .collect(Collectors.toList());

        List<String> instructors = userRepo.findAll()
                .stream()
                .map(UserRegistration::getFullName)
                .collect(Collectors.toList());

        return new BuildBatchDTO(courses, instructors, null);
    }

    public Batch saveBatch(int batchId, BatchFormDTO batchFormDTO) {
        Batch batch = convertDtoToEntity(batchId, batchFormDTO);
        return batchRepo.save(batch);
    }

    private Batch convertDtoToEntity(int batchId, BatchFormDTO batchFormDTO){
        Batch batch = new Batch();
        batch.setBatchId(batchId);
        batch.setBatchName(batchFormDTO.getBatchName());
        batch.setStartDate(batchFormDTO.getStartDate());
        batch.setEndDate(batchFormDTO.getEndDate());
        batch.setDays(batchFormDTO.getDays());
        batch.setStartTime(batchFormDTO.getStartTime());
        batch.setEndTime(batchFormDTO.getEndTime());
        batch.setLocation(batchFormDTO.getLocation());
        batch.setLocationAddress(batchFormDTO.getLocationAddress());
        Course course = courseRepo.findByName(batchFormDTO.getCourseName());
        UserRegistration instructor = userRepo.findById(batchFormDTO.getInstructorId()).orElse(null);

        batch.setCourse(course);
        batch.setUser(instructor);

        return batch;
    }
/*
    private BuildBatchDTO convertBatchToDto(Batch batch) {
        return new BuildBatchDTO(
                Collections.singletonList(batch.getCourse().getName()),
                Collections.singletonList(batch.getUser().getFullName()),
            new BatchFormDTO(
                batch.getBatchId(),
                batch.getBatchName(),
                batch.getStartDate(),
                batch.getEndDate(),
                batch.getDays(),
                batch.getStartTime(),
                batch.getEndTime(),
                batch.getLocation(),
                batch.getLocationAddress(),
                batch.getCourse().getName(),
                batch.getUser().getUserId()
            )
        );
    }*/
}