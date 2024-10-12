package com.goodquestion.edutrek_server.modules.group.persistence.groups;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "groups_archive")
@NoArgsConstructor
public class GroupArchiveEntity extends GroupEntity {

    @Column(name = "archivation_date")
    private LocalDate archivationDate;

    public GroupArchiveEntity(GroupEntity groupEntity) {
        super(groupEntity);
        this.archivationDate = LocalDate.now();
    }
}
