package com.goodquestion.edutrek_server.modules.group.persistence;

import java.time.LocalDate;
import java.util.UUID;

public interface IGroup {
    UUID getGroupId();
    String getGroupName();
    LocalDate getStartDate();
    LocalDate getFinishDate();
    Boolean getIsActive();
    UUID getCourseId();
    String getSlackLink();
    String getWhatsAppLink();
    String getSkypeLink();
    Boolean getDeactivateAfter();
}
