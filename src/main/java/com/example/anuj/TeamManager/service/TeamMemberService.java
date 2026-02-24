package com.example.anuj.TeamManager.service;

import com.example.anuj.TeamManager.dto.TeamMemberResponse;
import com.example.anuj.TeamManager.model.TeamRole;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TeamMemberService {
    TeamMemberResponse addMember(Long teamId, Authentication authentication);

    List<TeamMemberResponse> getMembers(Long teamId);

    TeamMemberResponse updateRole(Long teamId, Long userId, TeamRole role);

    String deleteMember(Long teamId, Long userId);
}
