package com.example.anuj.TeamManager.repository;

import com.example.anuj.TeamManager.model.Team;
import com.example.anuj.TeamManager.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember,Long> {
    @Query("SELECT tm.team FROM TeamMember tm WHERE tm.user.id = :userId")
    List<Team> findTeamsByUserId(@Param("userId") Long userId);
    Optional<TeamMember> findByUser_IdAndTeam_Id(Long userId, Long teamId);
    List<TeamMember> findByTeam_Id(Long teamId);
}
