package com.eco.environet.volunteering.repository;

import com.eco.environet.volunteering.model.VolunteerAction;
import com.eco.environet.volunteering.model.VolunteerActionRegisteredUser;
import com.eco.environet.volunteering.model.compositeKeys.VolunteerActionRegisteredUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VolunteerActionRegisteredUserRepository extends JpaRepository<VolunteerActionRegisteredUser, VolunteerActionRegisteredUserId> {
    List<VolunteerActionRegisteredUser> findByVolunteerActionIdAndAppearedFalse(Long volunteerActionId);
    List<VolunteerActionRegisteredUser> findByVolunteerActionIdAndAppearedTrue(Long volunteerActionId);
    List<VolunteerActionRegisteredUser> findByVolunteerActionId(Long volunteerActionId);
    int countByVolunteerActionId(Long volunteerActionId);
    boolean existsByUserIdAndVolunteerActionId(Long userId, Long volunteerActionId);
    @Query("SELECT va FROM VolunteerActionRegisteredUser varu JOIN varu.volunteerAction va WHERE varu.registeredUser.id = :userId AND va.date < CURRENT_DATE ORDER BY va.date DESC")
    List<VolunteerAction> findPastActionsByUserId(@Param("userId") Long userId);
    void deleteByUserIdAndVolunteerActionId(Long userId, Long volunteerActionId);
    @Query("SELECT v FROM VolunteerActionRegisteredUser v WHERE v.volunteerActionId = :volunteerActionId AND v.userId = :userId")
    VolunteerActionRegisteredUser findByVolunteerActionIdAndUserId(@Param("volunteerActionId") Long volunteerActionId, @Param("userId") Long userId);

    @Query("SELECT u FROM VolunteerActionRegisteredUser u WHERE u.volunteerActionId = :actionId AND u.points > 0")
    List<VolunteerActionRegisteredUser> findByVolunteerActionIdAndPointsGreaterThanZero(Long actionId);
}
