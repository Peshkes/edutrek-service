package com.goodquestion.edutrek_server.modules.group.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComposeStudentsKey implements Serializable {
    private UUID groupId;
    private UUID studentId;
}