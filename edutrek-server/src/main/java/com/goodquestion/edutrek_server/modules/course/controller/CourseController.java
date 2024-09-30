package com.goodquestion.edutrek_server.modules.course.controller;

import com.goodquestion.edutrek_server.modules.course.dto.CourseDataDto;
import com.goodquestion.edutrek_server.modules.course.persistence.CourseEntity;
import com.goodquestion.edutrek_server.modules.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseEntity> getAllCourses() {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CourseEntity getCourseById(@PathVariable @UUID String id) {
        return courseService.getCourseById(java.util.UUID.fromString(id));
    }

    @PostMapping("")
    public ResponseEntity<String> addNewCourse(@RequestBody @Valid CourseDataDto courseData) {
        courseService.addEntity(courseData);
        return new ResponseEntity<>("Course created", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourseById(@PathVariable @UUID String id) {
        courseService.deleteById(java.util.UUID.fromString(id));
        return new ResponseEntity<>("Course deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBranchById(@PathVariable @UUID String id, @RequestBody @Valid CourseDataDto courseData) {
        courseService.updateById(java.util.UUID.fromString(id), courseData);
        return new ResponseEntity<>("Course updated", HttpStatus.OK);
    }


}
