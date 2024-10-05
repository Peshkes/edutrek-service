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

    private final CourseRepository repository;

    public List<CourseEntity> getAll() {
        return repository.findAll();
    }

    public CourseEntity getById(UUID courseId) {
        return repository.findById(courseId).orElseThrow(() -> new ShareException.CourseNotFoundException(String.valueOf(courseId)));
    }

    @Transactional
    public void addEntity(CourseDataDto courseData) {
        try {
            repository.save(new CourseEntity(courseData.getCourseName(), courseData.getCourseAbbreviation()));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    public void deleteById(UUID courseId) {
        if (!repository.existsById(courseId))
            throw new ShareException.CourseNotFoundException(String.valueOf(courseId));

        try {
            repository.deleteById(courseId);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    public void updateById(UUID courseId, CourseDataDto courseData) {
        CourseEntity courseEntity = repository.findById(courseId).orElseThrow(() -> new ShareException.CourseNotFoundException(String.valueOf(courseId)));

        courseEntity.setCourseName(courseData.getCourseName());
        courseEntity.setCourseAbbreviation(courseData.getCourseAbbreviation());
        try {
            repository.save(courseEntity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }

    }
}
