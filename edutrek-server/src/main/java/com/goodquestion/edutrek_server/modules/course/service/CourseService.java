package com.goodquestion.edutrek_server.modules.course.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;
import com.goodquestion.edutrek_server.error.ShareException;
import com.goodquestion.edutrek_server.modules.course.dto.CourseDataDto;
import com.goodquestion.edutrek_server.modules.course.persistence.CourseEntity;
import com.goodquestion.edutrek_server.modules.course.persistence.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames={"courses"})
public class CourseService {

    private final CourseRepository repository;

    @Cacheable(key = "#root.methodName")
    public List<CourseEntity> getAll() {
        return repository.findAll();
    }

    public CourseEntity getById(UUID courseId) {
        return repository.findById(courseId).orElseThrow(() -> new ShareException.CourseNotFoundException(String.valueOf(courseId)));
    }

    @Transactional
    @CacheEvict(key = "{'getAll'}")
    public void addEntity(CourseDataDto courseData) {
        try {
            repository.save(new CourseEntity(courseData.getCourseName(), courseData.getCourseAbbreviation()));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    @CachePut(key = "#id")
    public void deleteById(UUID id) {
        if (!repository.existsById(id))
            throw new ShareException.CourseNotFoundException(String.valueOf(id));

        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    @CachePut(key = "#id")
    public void updateById(UUID id, CourseDataDto courseData) {
        CourseEntity courseEntity = repository.findById(id).orElseThrow(() -> new ShareException.CourseNotFoundException(String.valueOf(id)));

        courseEntity.setCourseName(courseData.getCourseName());
        courseEntity.setCourseAbbreviation(courseData.getCourseAbbreviation());
        try {
            repository.save(courseEntity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }

    }
}
