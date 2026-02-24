package com.example.anuj.TeamManager.controller;


import com.example.anuj.TeamManager.dto.TeamResponse;
import com.example.anuj.TeamManager.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/global")
public class GlobalController {

    private final TeamService teamService;

    @GetMapping("/teams")
    public ResponseEntity<Page<TeamResponse>> getAllTeams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ){
        return ResponseEntity.ok(teamService.getAllTeams(page,size,sortBy,sortDir));
    }
}
