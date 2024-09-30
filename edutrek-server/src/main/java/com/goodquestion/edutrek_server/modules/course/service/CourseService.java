package com.goodquestion.edutrek_server.modules.course.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;
import com.goodquestion.edutrek_server.error.ShareException;
import com.goodquestion.edutrek_server.modules.course.dto.CourseDataDto;
import com.goodquestion.edutrek_server.modules.course.persistence.CourseEntity;
import com.goodquestion.edutrek_server.modules.course.persistence.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<CourseEntity> getAllCourses() {
        return courseRepository.findAll();
    }

    public CourseEntity getCourseById(UUID courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new ShareException.CourseNotFoundException(String.valueOf(courseId)));
    }

    @Transactional
    public void addNewCourse(CourseDataDto courseData) {
        try {
            courseRepository.save(new CourseEntity(courseData.getCourseName(), courseData.getCourseAbbreviation()));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    public void deleteCourseById(UUID courseId) {
        if (!courseRepository.existsById(courseId))
            throw new ShareException.CourseNotFoundException(String.valueOf(courseId));

        try {
            courseRepository.deleteById(courseId);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    public void updateCourseById(UUID courseId, CourseDataDto courseData) {
        CourseEntity courseEntity = courseRepository.findById(courseId).orElseThrow(() -> new ShareException.CourseNotFoundException(String.valueOf(courseId)));

        courseEntity.setCourseName(courseData.getCourseName());
        courseEntity.setCourseAbbreviation(courseData.getCourseAbbreviation());
        try {
            courseRepository.save(courseEntity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }

    }
}
