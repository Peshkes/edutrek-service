package com.goodquestion.edutrek_server.modules.branch.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, Integer> {

    BranchEntity findByBranchName(String name);
}
