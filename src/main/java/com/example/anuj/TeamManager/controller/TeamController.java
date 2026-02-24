package com.example.anuj.TeamManager.controller;


import com.example.anuj.TeamManager.dto.TeamRequest;
import com.example.anuj.TeamManager.dto.TeamResponse;
import com.example.anuj.TeamManager.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody TeamRequest request, Authentication authentication){
        return ResponseEntity.ok(teamService.createTeam(request,authentication));
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getTeams(Authentication authentication){
        return ResponseEntity.ok(teamService.getTeam(authentication));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeamById(@PathVariable Long teamId){
        return ResponseEntity.ok(teamService.getTeamById(teamId));
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<?> patchTeamById(
            @PathVariable Long teamId,
            @RequestBody TeamRequest request)
    {
        return ResponseEntity.ok(teamService.patchTeamById(teamId,request));
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long teamId){
        return ResponseEntity.ok(teamService.deleteTeam(teamId));
    }
}
