package com.example.anuj.TeamManager.security;

import com.example.anuj.TeamManager.model.TeamRole;
import com.example.anuj.TeamManager.model.User;
import com.example.anuj.TeamManager.repository.TeamMemberRepository;
import com.example.anuj.TeamManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamSecurity {

    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;

    public boolean canUpdateRole(Long teamId, Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) return false;

        boolean result =  teamMemberRepository
                .findByUser_IdAndTeam_Id(user.getId(), teamId)
                .map(member -> member.getRole() == TeamRole.LEADER)
                .orElse(false);
        System.out.println(result);
        return result;
    }
}