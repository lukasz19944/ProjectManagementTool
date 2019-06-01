package pl.slusarski.springbootprojectmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.slusarski.springbootprojectmanagement.domain.Backlog;
import pl.slusarski.springbootprojectmanagement.domain.Project;
import pl.slusarski.springbootprojectmanagement.domain.User;
import pl.slusarski.springbootprojectmanagement.exception.ProjectIdException;
import pl.slusarski.springbootprojectmanagement.exception.ProjectNotFoundException;
import pl.slusarski.springbootprojectmanagement.repository.BacklogRepository;
import pl.slusarski.springbootprojectmanagement.repository.ProjectRepository;
import pl.slusarski.springbootprojectmanagement.repository.UserRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username) {

        String projectIdentifier = project.getProjectIdentifier().toUpperCase();

        if (project.getId() != null) {
            Project existingProject = projectRepository.findByProjectIdentifier(projectIdentifier);

            if (existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
                throw new ProjectNotFoundException("Project not found in your account");
            } else if (existingProject == null) {
                throw new ProjectNotFoundException("Project with ID: " + project.getProjectIdentifier() + " cannot be updated because it does not exist");
            }
        }

        try {
            User user = userRepository.findByUsername(username);

            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(projectIdentifier);

            // backlog jest tworzony tylko przy tworzeniu nowego obiektu, a nie edycji
            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(projectIdentifier);
            }

            if (project.getId() != null) {
                project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
            }

            return projectRepository.save(project);
        } catch (Exception exc) {
            throw new ProjectIdException("Project ID " + projectIdentifier + " already exists");
        }

    }

    public Project findProjectByIdentifier(String projectId, String username) {

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project ID " + projectId.toUpperCase() + " does not exist");
        }

        if (!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not found in your account");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username) {
        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }
}
