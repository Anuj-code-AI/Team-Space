package com.example.anuj.TeamManager.dto;

import com.example.anuj.TeamManager.model.TeamRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TeamMemberResponse {
    private Long memberId;
    private String teamName;
    private TeamRole role;
    private LocalDateTime joinedAt;
}
