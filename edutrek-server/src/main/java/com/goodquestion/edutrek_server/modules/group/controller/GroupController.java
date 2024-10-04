package com.goodquestion.edutrek_server.modules.group.controller;

import com.goodquestion.edutrek_server.modules.group.dto.AddGroupDto;
import com.goodquestion.edutrek_server.modules.group.dto.GroupDto;
import com.goodquestion.edutrek_server.modules.group.dto.PaginationResponse;
import com.goodquestion.edutrek_server.modules.group.dto.UpdateGroupDto;
import com.goodquestion.edutrek_server.modules.group.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<GroupDto> getAllGroups() {
        return groupService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GroupDto getGroupById(@PathVariable @UUID String id) {
        return groupService.getById(java.util.UUID.fromString(id));
    }

    @GetMapping("/paginated")
    @ResponseStatus(HttpStatus.OK)
    public PaginationResponse getAllGroupsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) UUID courseId,
            @RequestParam(required = false) int statusId
    ) {
        return groupService.getAllPaginated(page, size, courseId, statusId);
    }

    @PostMapping("")
    public ResponseEntity<String> addNewGroup(@RequestBody @Valid AddGroupDto groupData) {
        groupService.addEntity(groupData);
        return new ResponseEntity<>("Group created", HttpStatus.CREATED);
    }

    @PostMapping("/students/{id}")
    public ResponseEntity<String> addStudentsToGroup(@PathVariable @UUID String id, @RequestBody List<java.util.UUID> students) {
        groupService.addStudentsToGroup(java.util.UUID.fromString(id), students);
        return new ResponseEntity<>("Students added", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroupById(@PathVariable @UUID String id) {
        groupService.deleteById(java.util.UUID.fromString(id));
        return new ResponseEntity<>("Group deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateGroupById(@PathVariable @UUID String id, @RequestBody @Valid UpdateGroupDto groupData) {
        groupService.updateById(java.util.UUID.fromString(id), groupData);
        return new ResponseEntity<>("Group updated", HttpStatus.OK);
    }

    @PutMapping("/graduate/{id}")
    public ResponseEntity<String> graduateGroupById(@PathVariable @UUID String id) {
        groupService.graduateById(java.util.UUID.fromString(id));
        return new ResponseEntity<>("Group graduated", HttpStatus.OK);
    }

    @PutMapping("/{fromId}/move/{toId}")
    public ResponseEntity<String> moveStudentsBetweenGroups(@PathVariable @UUID String fromId, @PathVariable @UUID String toId, @RequestBody List<java.util.UUID> students) {
        groupService.moveStudentsBetweenGroups(fromId, toId, students);
        return new ResponseEntity<>("Group created", HttpStatus.CREATED);
    }

    @PutMapping("/archive/students/{id}")
    public ResponseEntity<String> archiveStudents(@PathVariable @UUID String id, @RequestBody @Valid List<java.util.UUID> students) {
        groupService.archiveStudents(id, students);
        return new ResponseEntity<>("Students archived", HttpStatus.CREATED);
    }

    @PutMapping("/lecturers/{id}")
    public ResponseEntity<String> addLecturersToGroup(@PathVariable @UUID String id, @RequestBody @Valid List<java.util.UUID> lecturers) {
        groupService.changeLecturersToGroup(java.util.UUID.fromString(id), lecturers);
        return new ResponseEntity<>("Lecturers added", HttpStatus.CREATED);
    }
}
