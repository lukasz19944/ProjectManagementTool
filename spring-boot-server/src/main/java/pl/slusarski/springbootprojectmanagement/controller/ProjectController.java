package pl.slusarski.springbootprojectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.slusarski.springbootprojectmanagement.domain.Project;
import pl.slusarski.springbootprojectmanagement.service.MapValidationErrorService;
import pl.slusarski.springbootprojectmanagement.service.ProjectService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal) {

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);

        if (errorMap != null) {
            return errorMap;
        }

        Project p = projectService.saveOrUpdateProject(project, principal.getName());

        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal) {

        Project p = projectService.findProjectByIdentifier(projectId, principal.getName());

        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @GetMapping("")
    public Iterable<Project> getAllProjects(Principal principal) {
        return projectService.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal) {
        projectService.deleteProjectByIdentifier(projectId, principal.getName());

        return new ResponseEntity<>("Project with ID " + projectId.toUpperCase() + " was deleted", HttpStatus.OK);
    }
}
