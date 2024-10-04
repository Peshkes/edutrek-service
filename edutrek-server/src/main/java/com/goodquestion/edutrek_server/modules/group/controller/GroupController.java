package com.goodquestion.edutrek_server.modules.group.controller;

import com.goodquestion.edutrek_server.modules.group.dto.AddGroupDto;
import com.goodquestion.edutrek_server.modules.group.dto.GroupDto;
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

    @PostMapping("")
    public ResponseEntity<String> addNewGroup(@RequestBody @Valid AddGroupDto groupData) {
        groupService.addEntity(groupData);
        return new ResponseEntity<>("Group created", HttpStatus.CREATED);
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


}
