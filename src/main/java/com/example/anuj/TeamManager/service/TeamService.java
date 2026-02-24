package com.example.anuj.TeamManager.service;

import com.example.anuj.TeamManager.dto.TeamRequest;
import com.example.anuj.TeamManager.dto.TeamResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TeamService {
    TeamResponse createTeam(TeamRequest request, Authentication authentication);
    List<TeamResponse> getTeam(Authentication authentication);
    TeamResponse getTeamById(Long teamId);
    TeamResponse patchTeamById(Long teamId, TeamRequest request);
    String deleteTeam(Long teamId);
    Page<TeamResponse> getAllTeams(int page, int size, String sortBy, String sortDir);
}
