package com.goodquestion.edutrek_server.modules.group.persistence;

import org.springframework.data.jpa.domain.Specification;

public class GroupSpecifications {

    public static Specification<GroupEntity> hasCourseId(String courseId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("course").get("id"), courseId);
    }

    public static Specification<GroupEntity> hasIsActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<GroupEntity> searchByQuery(String searchQuery) {
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + searchQuery.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("groupName")), likePattern)
            );
        };
    }
}
