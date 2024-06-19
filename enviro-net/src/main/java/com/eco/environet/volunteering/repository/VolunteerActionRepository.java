package com.eco.environet.volunteering.repository;

import com.eco.environet.volunteering.model.VolunteerAction;
import com.eco.environet.volunteering.model.VolunteerActionStatus;
import jakarta.transaction.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerActionRepository extends JpaRepository<VolunteerAction, Long> {
    List<VolunteerAction> findByCreatorId(Long creatorId);
    List<VolunteerAction> findByProjectId(Long projectId);
    List<VolunteerAction> findBySupervisorId(Long supervisorId);
    @Query("SELECT va FROM VolunteerAction va WHERE va.date >= CURRENT_DATE ORDER BY va.date ASC")
    List<VolunteerAction> findUpcomingActions();
}
