package com.smarttask.controller;

import com.smarttask.entity.Team;
import com.smarttask.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Team> createTeam(
            @RequestParam String name,
            @RequestParam String description
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teamService.createTeam(name, description));
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @PostMapping("/{teamId}/members/{userId}")
    public ResponseEntity<Team> addMember(
            @PathVariable Long teamId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(teamService.addMemberToTeam(teamId, userId));
    }
}