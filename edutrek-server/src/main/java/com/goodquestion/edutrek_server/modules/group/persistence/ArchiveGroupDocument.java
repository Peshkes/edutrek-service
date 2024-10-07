package com.goodquestion.edutrek_server.modules.group.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Document("groups_archive")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveGroupDocument implements IGroup {
    @Id
    private UUID groupId;
    private String groupName;
    private LocalDate startDate;
    private LocalDate finishDate;
    private Boolean isActive;
    private UUID courseId;
    private String slackLink;
    private String whatsAppLink;
    private String skypeLink;
    private Boolean deactivateAfter;
    private LocalDate archiveDate;

    public ArchiveGroupDocument(IGroup group) {
        this.groupId = group.getGroupId();
        this.groupName = group.getGroupName();
        this.startDate = group.getStartDate();
        this.finishDate = group.getFinishDate();
        this.isActive = group.getIsActive();
        this.courseId = group.getCourseId();
        this.slackLink = group.getSlackLink();
        this.whatsAppLink = group.getWhatsAppLink();
        this.skypeLink = group.getSkypeLink();
        this.deactivateAfter = group.getDeactivateAfter();
        this.archiveDate = LocalDate.now();
    }
}
