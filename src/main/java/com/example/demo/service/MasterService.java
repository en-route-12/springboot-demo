package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private final StudentRepository studentRepo;

    public MasterService(LoginRepository loginRepo, CourseRepository courseRepo, UserRegistrationRepository userRepo, BatchRepository batchRepo, StudentRepository studentRepo) {
        this.loginRepo = loginRepo;
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
        this.batchRepo = batchRepo;
        this.studentRepo = studentRepo;
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

    public Batch saveBatch(BatchFormDTO batchFormDTO) {
        Batch batch = convertDtoToEntity(batchFormDTO);
        return batchRepo.save(batch);
    }

    private Batch convertDtoToEntity(BatchFormDTO batchFormDTO){
        Batch batch = new Batch();
        System.out.println("Batch ID before saving: " + batchFormDTO.getBatchId());
        batch.setBatchId(batchFormDTO.getBatchId());
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

    @Data
    public static class BatchInfoDTO {
        private String course;
        private String batchName;
        private String timeslot;
        private int batchLimit;
        private int currentStudents;
        private String instructorName;
        private String venue;
        private String description;
    }

    public BatchInfoDTO getBatchInfoByBatchId(int batchId) {
        Optional<Batch> batch = batchRepo.findById(batchId);

        return batch.map(this::convertBatchEntityToBatchInfoDto).orElse(null);
    }

    private BatchInfoDTO convertBatchEntityToBatchInfoDto(Batch batch) {
        BatchInfoDTO batchInfoDTO = new BatchInfoDTO();
        batchInfoDTO.setBatchName(batch.getBatchName());
        batchInfoDTO.setTimeslot(batch.getStartTime() + " - " + batch.getEndTime());
        batchInfoDTO.setBatchLimit(batch.getBatchLimit());
        batchInfoDTO.setCurrentStudents(batch.getCurrentStudents());
        batchInfoDTO.setInstructorName(batch.getUser().getFullName());
        batchInfoDTO.setVenue(batch.getLocation());
        batchInfoDTO.setDescription(batch.getLocationAddress());
        batchInfoDTO.setCourse(batch.getCourse().getName());

        return batchInfoDTO;
    }

    @Data
    public static class StudentPaymentDetailDTO {
        private int batchId;
        private String className;
        private String batchName;
        private String timeslot;
        private List<StudentPaymentListDTO>  studentDetails;
    }

    @Data
    public static class StudentPaymentListDTO {
        private int id;
        private String profileImg;
        private String studentName;
        private List<String> button;
    }

    public List<StudentPaymentDetailDTO> getStudentDetails(int batchId) {
        List<Student> students = studentRepo.findByBatch(batchRepo.findById(batchId).orElse(null));

        return students.stream()
                .collect(Collectors.groupingBy(student -> student.getBatch().getBatchId()))
                .values()
                .stream()
                .map(studentList -> {
                    Batch batch = studentList.getFirst().getBatch();

                    StudentPaymentDetailDTO batchDTO = new StudentPaymentDetailDTO();
                    batchDTO.setBatchId(batch.getBatchId());
                    batchDTO.setClassName(batch.getCourse().getName());
                    batchDTO.setBatchName(batch.getBatchName());
                    batchDTO.setTimeslot(batch.getStartTime() + " - " + batch.getEndTime());

                    List<StudentPaymentListDTO> studentDetails = studentList.stream().map(student -> {
                        StudentPaymentListDTO studentDTO = new StudentPaymentListDTO();
                        studentDTO.setId(student.getId());
                        studentDTO.setStudentName(student.getStudentName());
                        studentDTO.setProfileImg(student.getProfileImg());
                        studentDTO.setButton(new ArrayList<>(student.getButton()));
                        return studentDTO;
                    }).collect(Collectors.toList());

                    batchDTO.setStudentDetails(studentDetails);
                    return batchDTO;
                })
                .collect(Collectors.toList());
    }

/*
    public List<StudentPaymentDetailDTO> getStudentDetails(int batchId){
        Batch batch = batchRepo.findById(batchId).orElse(null);
            return studentRepo.findByBatch(batch)
                    .stream()
                    .map(this::convertStudentEntityToDto)
                    .collect(Collectors.toList());
    }

    public StudentPaymentDetailDTO convertStudentEntityToDto(Student student){
        StudentPaymentDetailDTO studentPaymentDetailDTO = new StudentPaymentDetailDTO();
        StudentPaymentListDTO studentPaymentListDTO = new StudentPaymentListDTO();

        studentPaymentListDTO.setId(student.getId());
        studentPaymentListDTO.setStudentName(student.getStudentName());
        studentPaymentListDTO.setProfileImg(student.getProfileImg());
        studentPaymentListDTO.setButton(student.getButton());

        List<StudentPaymentListDTO> studentDetails = Collections.singletonList(studentPaymentListDTO);

        studentPaymentDetailDTO.setBatchId(student.getBatch().getBatchId());
        studentPaymentDetailDTO.setClassName(student.getCourseName());
        studentPaymentDetailDTO.setBatchName(student.getBatchName());
        studentPaymentDetailDTO.setTimeslot(student.getTimeslot());
        studentPaymentDetailDTO.setStudentDetails(studentDetails);

        return studentPaymentDetailDTO;
    }
*/
}