package com.example.anuj.TeamManager.service;

import com.example.anuj.TeamManager.dto.TeamRequest;
import com.example.anuj.TeamManager.dto.TeamResponse;
import com.example.anuj.TeamManager.model.Team;
import com.example.anuj.TeamManager.model.TeamMember;
import com.example.anuj.TeamManager.model.TeamRole;
import com.example.anuj.TeamManager.model.User;
import com.example.anuj.TeamManager.repository.TeamMemberRepository;
import com.example.anuj.TeamManager.repository.TeamRepository;
import com.example.anuj.TeamManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    public final UserRepository userRepository;
    public final TeamRepository teamRepository;
    public final TeamMemberRepository teamMemberRepository;

    @Override
    public TeamResponse createTeam(TeamRequest request, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("user not found"));
        if(teamRepository.findByName(request.getName()).isPresent()){
            throw new RuntimeException("Team already exist. Use some unique name");
        }
        Team team = teamRepository.save(new Team(request.getName()));
        teamMemberRepository.save(
                new TeamMember(null,user,team, TeamRole.LEADER)
        );
        return new TeamResponse(team.getId(),team.getName(),team.getMembers());
    }

    @Override
    public List<TeamResponse> getTeam(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("user not found"));
        List<Team> teams = teamMemberRepository.findTeamsByUserId(user.getId());
        return teams.stream()
                .map(team -> new TeamResponse(
                        team.getId(),
                        team.getName(),
                        team.getMembers()
                ))
                .toList();
    }

    @Override
    public TeamResponse getTeamById(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(()-> new RuntimeException("Team not found"));
        return new TeamResponse(team.getId(),team.getName(),team.getMembers());
    }

    @Override
    public TeamResponse patchTeamById(Long teamId, TeamRequest request) {
        Team currTeam = teamRepository.findById(teamId)
                .orElseThrow(()-> new RuntimeException("Team not found"));
        currTeam.setName(request.getName());
        teamRepository.save(currTeam);
        return new TeamResponse(currTeam.getId(),currTeam.getName(),currTeam.getMembers());
    }

    @Override
    public String deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(()-> new RuntimeException("Team not found"));
        teamRepository.delete(team);
        return "team successfully deleted";
    }

    @Override
    public Page<TeamResponse> getAllTeams(int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Team> teamPage = teamRepository.findAll(pageRequest);

        return teamPage.map(team ->
                new TeamResponse(
                        team.getId(),
                        team.getName(),
                        team.getMembers()
                )
        );
    }
}
