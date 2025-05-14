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
        private String batchId;
        private String batchName;
        private LocalTime startTime;
        private LocalTime endTime;
    }

    public List<BatchCardsDTO> getBatchesByCourse(String course){
        Optional<Batch> batch = batchRepo.findById(course);
        return batchRepo.findByCourse(course)
                .stream()
                .map(this::getBatchCardsToEntity)
                .collect(Collectors.toList());
    }

    public BatchCardsDTO getBatchCardsToEntity(Batch batch){
        BatchCardsDTO batchCardsDTO = new BatchCardsDTO();
        batchCardsDTO.setBatchId(batch.getBatchId());
        batchCardsDTO.setBatchName(batchCardsDTO.getBatchName());
        batchCardsDTO.setStartTime(batch.getStartTime());
        batchCardsDTO.setEndTime(batch.getEndTime());
        return batchCardsDTO;
    }


    @Data
    @AllArgsConstructor
    public static class BatchFormDTO {
        private String batchId;
        private String batchName;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<String> days;
        private LocalTime startTime;
        private LocalTime endTime;
        private String location;
        private String locationAddress;
    }

    @Data
    public static class BuildBatchDTO {
        private CourseDTO course;
        private UserRegistrationDTO instructorName;
        private BatchFormDTO batchFormDTO;
    }
/*
    public List<BuildBatchDTO> getBatchesByCourseId(String courseId) {
        Optional<Course> course = courseRepo.findById(courseId);
        return batchRepo.findByCourse(course)
                .stream()
                .map(this::convertBatchToDto)
                .collect(Collectors.toList());
    }

    public void saveBatch(BatchFormDTO batchFormDTO) {
        Batch batch = new Batch();
        batch.setBatchId(batchFormDTO.getBatchId());
        batch.setBatchName(batchFormDTO.getBatchName());
        batch.setStartDate(batchFormDTO.getStartDate());
        batch.setEndDate(batchFormDTO.getEndDate());
        batch.setDays(batchFormDTO.getDays());
        batch.setStartTime(batchFormDTO.getStartTime());
        batch.setEndTime(batchFormDTO.getEndTime());
        batch.setLocation(batchFormDTO.getLocation());
        batch.setLocationAddress(batchFormDTO.getLocationAddress());
        batchRepo.save(batch);
    }

    private BuildBatchDTO convertBatchToDto(Batch batch) {
        BuildBatchDTO buildBatchDTO = new BuildBatchDTO();

        buildBatchDTO.setBatchFormDTO(new BatchFormDTO(
                batch.getBatchId(),
                batch.getBatchName(),
                batch.getStartDate(),
                batch.getEndDate(),
                batch.getDays(),
                batch.getStartTime(),
                batch.getEndTime(),
                batch.getLocation(),
                batch.getLocationAddress()
        ));

        List<Course> course = batch.getCourse().stream()
                .map(Course::getName)
                .collect(Collectors.toList());
        buildBatchDTO.setCourse(course);

        List<UserRegistration> instructorName = batch.getInstructor().stream()
                .map(UserRegistration::getFullName)
                .collect(Collectors.toList());
        buildBatchDTO.setInstructorName(instructorName);
        return buildBatchDTO;
    }*/
}