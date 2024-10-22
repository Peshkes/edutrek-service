package com.goodquestion.edutrek_server.modules.statuses.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface StatusRepository extends JpaRepository<StatusEntity, Integer> {

    StatusEntity findStatusEntityByStatusName(String status);
}
