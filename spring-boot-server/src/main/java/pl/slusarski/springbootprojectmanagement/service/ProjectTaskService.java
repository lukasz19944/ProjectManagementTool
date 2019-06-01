package pl.slusarski.springbootprojectmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.slusarski.springbootprojectmanagement.domain.Backlog;
import pl.slusarski.springbootprojectmanagement.domain.Project;
import pl.slusarski.springbootprojectmanagement.domain.ProjectTask;
import pl.slusarski.springbootprojectmanagement.exception.ProjectNotFoundException;
import pl.slusarski.springbootprojectmanagement.repository.BacklogRepository;
import pl.slusarski.springbootprojectmanagement.repository.ProjectRepository;
import pl.slusarski.springbootprojectmanagement.repository.ProjectTaskRepository;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();

            projectTask.setBacklog(backlog);

            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;

            backlog.setPTSequence(backlogSequence);

            projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3);
            }

            if (projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);


    }

    public Iterable<ProjectTask> findBacklogById(String id, String username) {

        projectService.findProjectByIdentifier(id, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findProjectTaskByProjectSequence(String backlogId, String projectTaskId, String username) {

        projectService.findProjectByIdentifier(backlogId, username);

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(projectTaskId);

        if (projectTask == null) {
            throw new ProjectNotFoundException("Project task with ID " + projectTaskId.toUpperCase()+ " not found");
        }

        if (!projectTask.getProjectIdentifier().equals(backlogId)) {
            throw new ProjectNotFoundException("Project task with ID " + projectTaskId.toUpperCase()+ " does not exist in project " + backlogId.toUpperCase());
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String projectTaskId, String username) {
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlogId, projectTaskId, username);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deleteProjectTaskByProjectSequence(String backlogId, String projectTaskId, String username) {
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlogId, projectTaskId, username);

        projectTaskRepository.delete(projectTask);
    }
}
