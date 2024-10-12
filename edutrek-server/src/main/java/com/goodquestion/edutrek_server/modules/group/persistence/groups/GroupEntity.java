package com.goodquestion.edutrek_server.modules.group.persistence.groups;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class GroupEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "group_id")
    private UUID groupId;
    @Setter
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Setter
    @Column(name = "finish_date")
    private LocalDate finishDate;
    @Setter
    @Column(name = "is_active")
    private Boolean isActive;
    @Setter
    @Column(name = "course_id")
    private UUID courseId;
    @Setter
    @Column(name = "slack_link")
    private String slackLink;
    @Setter
    @Column(name = "whats_app_link")
    private String whatsAppLink;
    @Setter
    @Column(name = "skype_link")
    private String skypeLink;
    @Setter
    @Column(name = "deactivate_after")
    private Boolean deactivateAfter;

    public GroupEntity(String groupName, LocalDate startDate, LocalDate finishDate, Boolean isActive, UUID courseId, String slackLink, String whatsAppLink, String skypeLink, Boolean deactivateAfter) {
        this.groupId = UUID.randomUUID();
        this.groupName = groupName;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.isActive = isActive;
        this.courseId = courseId;
        this.slackLink = slackLink;
        this.whatsAppLink = whatsAppLink;
        this.skypeLink = skypeLink;
        this.deactivateAfter = deactivateAfter;
    }

    public GroupEntity(GroupEntity groupEntity) {
        this.groupId = groupEntity.getGroupId();
        this.groupName = groupEntity.getGroupName();
        this.startDate = groupEntity.getStartDate();
        this.finishDate = groupEntity.getFinishDate();
        this.isActive = groupEntity.getIsActive();
        this.courseId = groupEntity.getCourseId();
        this.slackLink = groupEntity.getSlackLink();
        this.whatsAppLink = groupEntity.getWhatsAppLink();
        this.skypeLink = groupEntity.getSkypeLink();
        this.deactivateAfter = groupEntity.getDeactivateAfter();
    }
}
