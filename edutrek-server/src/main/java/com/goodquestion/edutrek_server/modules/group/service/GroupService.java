package com.goodquestion.edutrek_server.modules.group.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;
import com.goodquestion.edutrek_server.error.ShareException;
import com.goodquestion.edutrek_server.modules.course.persistence.CourseRepository;
import com.goodquestion.edutrek_server.modules.group.dto.AddGroupDto;
import com.goodquestion.edutrek_server.modules.group.dto.GroupDto;
import com.goodquestion.edutrek_server.modules.group.dto.UpdateGroupDto;
import com.goodquestion.edutrek_server.modules.group.persistence.GroupEntity;
import com.goodquestion.edutrek_server.modules.group.persistence.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;
    private final CourseRepository courseRepository;

    public List<GroupDto> getAll() {
        List<GroupEntity> groupEntities = repository.findAll();
        if (!groupEntities.isEmpty()){
            return groupEntities.stream().map(this::constructDto).toList();
        } else
            return List.of();
    }

    public GroupDto getById(UUID groupId) {
        return constructDto(repository.findById(groupId).orElseThrow(() -> new ShareException.GroupNotFoundException(String.valueOf(groupId))));
    }

    @Transactional
    public void addEntity(AddGroupDto groupData) {
        try {
            repository.save(constructEntity(groupData));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    public void deleteById(UUID groupId) {
        if (!repository.existsById(groupId))
            throw new ShareException.CourseNotFoundException(String.valueOf(groupId));

        try {
            repository.deleteById(groupId);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    public void updateById(UUID groupId, UpdateGroupDto groupData) {
        GroupEntity groupEntity = repository.findById(groupId).orElseThrow(() -> new ShareException.CourseNotFoundException(String.valueOf(groupId)));

        groupEntity.setGroupName(groupData.getGroupName());
        groupEntity.setFinishDate(groupData.getFinishDate());
        groupEntity.setIsActive(groupData.getIsActive());
        groupEntity.setCourse(courseRepository.findById(groupData.getCourseId())
                .orElseThrow(() -> new ShareException.CourseNotFoundException(groupData.getCourseId().toString())));
        groupEntity.setSlackLink(groupData.getSlackLink());
        groupEntity.setWhatsAppLink(groupData.getWhatsAppLink());
        groupEntity.setSkypeLink(groupData.getSkypeLink());
        groupEntity.setDeactivateAfter(groupData.getDeactivateAfter());

        try {
            repository.save(groupEntity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }

    }

    //UTILITY

    private GroupDto constructDto(GroupEntity groupEntity) {
        return new GroupDto(
                groupEntity.getGroupId(),
                groupEntity.getGroupName(),
                groupEntity.getStartDate(),
                groupEntity.getFinishDate(),
                groupEntity.getIsActive(),
                groupEntity.getCourse().getCourseId(),
                groupEntity.getSlackLink(),
                groupEntity.getWhatsAppLink(),
                groupEntity.getSkypeLink(),
                groupEntity.getDeactivateAfter()
        );
    }

    private GroupEntity constructEntity(GroupDto groupDto) {
        return new GroupEntity(
                groupDto.getGroupId(),
                groupDto.getGroupName(),
                groupDto.getStartDate(),
                groupDto.getFinishDate(),
                groupDto.getIsActive(),
                courseRepository.findById(groupDto.getCourseId())
                        .orElseThrow(() -> new ShareException.CourseNotFoundException(groupDto.getCourseId().toString())),
                groupDto.getSlackLink(),
                groupDto.getWhatsAppLink(),
                groupDto.getSkypeLink(),
                groupDto.getDeactivateAfter()
        );
    }

    private GroupEntity constructEntity(AddGroupDto groupDto) {
        return new GroupEntity(
                groupDto.getGroupName(),
                groupDto.getStartDate(),
                groupDto.getFinishDate(),
                groupDto.getIsActive(),
                courseRepository.findById(groupDto.getCourseId())
                        .orElseThrow(() -> new ShareException.CourseNotFoundException(groupDto.getCourseId().toString())),
                groupDto.getSlackLink(),
                groupDto.getWhatsAppLink(),
                groupDto.getSkypeLink(),
                groupDto.getDeactivateAfter()
        );
    }
}
