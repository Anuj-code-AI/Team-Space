package com.example.anuj.TeamManager.controller;

import com.example.anuj.TeamManager.model.TeamRole;
import com.example.anuj.TeamManager.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teams")
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @PostMapping("/{teamId}/members")
    public ResponseEntity<?> addMembers(@PathVariable Long teamId, Authentication authentication){
        return ResponseEntity.ok(teamMemberService.addMember(teamId,authentication));
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<?> getMembers(@PathVariable Long teamId){
        return ResponseEntity.ok(teamMemberService.getMembers(teamId));
    }

    @PatchMapping("/{teamId}/members/{userId}")
    @PreAuthorize("@teamSecurity.canUpdateRole(#teamId, authentication)")
    public ResponseEntity<?> updateRole(
            @PathVariable Long teamId,
            @PathVariable Long userId,
            @RequestBody TeamRole role
    ){
        return ResponseEntity.ok(
                teamMemberService.updateRole(teamId, userId, role)
        );
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    @PreAuthorize("@teamSecurity.canUpdateRole(#teamId, authentication)")
    public ResponseEntity<?> deleteRole(
            @PathVariable Long teamId,
            @PathVariable Long userId
    ){
        return ResponseEntity.ok(
                teamMemberService.deleteMember(teamId, userId)
        );
    }
}

