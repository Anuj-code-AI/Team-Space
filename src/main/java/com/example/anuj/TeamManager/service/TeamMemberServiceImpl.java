package com.example.anuj.TeamManager.service;


import com.example.anuj.TeamManager.dto.TeamMemberResponse;
import com.example.anuj.TeamManager.model.Team;
import com.example.anuj.TeamManager.model.TeamMember;
import com.example.anuj.TeamManager.model.TeamRole;
import com.example.anuj.TeamManager.model.User;
import com.example.anuj.TeamManager.repository.TeamMemberRepository;
import com.example.anuj.TeamManager.repository.TeamRepository;
import com.example.anuj.TeamManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService{
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Override
    public TeamMemberResponse addMember(Long teamId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(()->new RuntimeException("Team not exist"));
        TeamMember member = teamMemberRepository.save(new TeamMember(null,user,team, TeamRole.MEMBER));
        return new TeamMemberResponse(
                team.getId(),
                team.getName(),
                TeamRole.MEMBER,
                LocalDateTime.now()
        );
    }

    @Override
    public List<TeamMemberResponse> getMembers(Long teamId) {

        List<TeamMember> memberList =
                teamMemberRepository.findByTeam_Id(teamId);

        return memberList.stream()
                .map(member -> new TeamMemberResponse(
                        member.getTeam_member_id(),
                        member.getTeam().getName(),
                        member.getRole(),
                        LocalDateTime.now()
                ))
                .toList();
    }

    @Override
    public TeamMemberResponse updateRole(Long teamId,
                                         Long userId,
                                         TeamRole newRole) {

        TeamMember member = teamMemberRepository
                .findByUser_IdAndTeam_Id(userId, teamId)
                .orElseThrow(() ->
                        new RuntimeException("Member not found in this team")
                );

        member.setRole(newRole);

        teamMemberRepository.save(member);

        return new TeamMemberResponse(
                member.getUser().getId(),
                member.getTeam().getName(),
                member.getRole(),
                LocalDateTime.now()   // better if you store joinedAt in entity
        );
    }

    @Override
    public String deleteMember(Long teamId, Long userId) {
        TeamMember member = teamMemberRepository
                .findByUser_IdAndTeam_Id(userId, teamId)
                .orElseThrow(() ->
                        new RuntimeException("Member not found in this team")
                );
        teamMemberRepository.delete(member);
        return "Member Successfully deleted";
    }
}
