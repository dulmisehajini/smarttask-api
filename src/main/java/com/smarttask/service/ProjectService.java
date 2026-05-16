package com.smarttask.service;

import com.smarttask.entity.Project;
import com.smarttask.entity.ProjectStatus;
import com.smarttask.entity.Team;
import com.smarttask.repository.ProjectRepository;
import com.smarttask.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;

    public Project createProject(String name, String description, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Project project = Project.builder()
                .name(name)
                .description(description)
                .team(team)
                .status(ProjectStatus.ACTIVE)
                .build();

        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }
}