package com.goodquestion.edutrek_server.modules.group.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComposeLecturerKey implements Serializable {
    private UUID lecturerId;
    private UUID groupId;
}