package com.goodquestion.edutrek_server.modules.group.persistence.groups;

import org.springframework.data.jpa.domain.Specification;

public class GroupSpecifications {

    public static <T extends BaseGroup> Specification<T> hasCourseId(String courseId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("course").get("courseId"), courseId);
    }

    public static <T extends BaseGroup> Specification<T> hasIsActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static <T extends BaseGroup> Specification<T> searchByQuery(String searchQuery) {
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + searchQuery.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("groupName")), likePattern)
            );
        };
    }
}
