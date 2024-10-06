package com.goodquestion.edutrek_server.modules.group.persistence;

import com.goodquestion.edutrek_server.modules.course.persistence.CourseEntity;
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
public class GroupEntity {
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
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private CourseEntity course;
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

    public GroupEntity(String groupName, LocalDate startDate, LocalDate finishDate, Boolean isActive,  CourseEntity course, String slackLink, String whatsAppLink, String skypeLink, Boolean deactivateAfter) {
        this.groupName = groupName;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.isActive = isActive;
        this.course = course;
        this.slackLink = slackLink;
        this.whatsAppLink = whatsAppLink;
        this.skypeLink = skypeLink;
        this.deactivateAfter = deactivateAfter;
    }
}
