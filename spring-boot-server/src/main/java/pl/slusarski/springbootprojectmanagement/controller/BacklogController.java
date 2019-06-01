package pl.slusarski.springbootprojectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.slusarski.springbootprojectmanagement.domain.Project;
import pl.slusarski.springbootprojectmanagement.domain.ProjectTask;
import pl.slusarski.springbootprojectmanagement.service.MapValidationErrorService;
import pl.slusarski.springbootprojectmanagement.service.ProjectTaskService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlogId}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                     BindingResult result, @PathVariable String backlogId,
                                                     Principal principal) {

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);

        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask pt = projectTaskService.addProjectTask(backlogId, projectTask, principal.getName());

        return new ResponseEntity<>(pt, HttpStatus.CREATED);
    }

    @GetMapping("/{backlogId}")
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlogId, Principal principal) {
        return projectTaskService.findBacklogById(backlogId, principal.getName());
    }

    @GetMapping("/{backlogId}/{projectTaskId}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlogId, @PathVariable String projectTaskId, Principal principal) {

        ProjectTask projectTask = projectTaskService.findProjectTaskByProjectSequence(backlogId, projectTaskId, principal.getName());

        return new ResponseEntity<>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("{backlogId}/{projectTaskId}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                               @PathVariable String backlogId, @PathVariable String projectTaskId,
                                               Principal principal) {

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);

        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask updatedProjectTask = projectTaskService.updateByProjectSequence(projectTask, backlogId, projectTaskId, principal.getName());

        return new ResponseEntity<>(updatedProjectTask, HttpStatus.OK);
    }

    @DeleteMapping("{backlogId}/{projectTaskId}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlogId, @PathVariable String projectTaskId, Principal principal) {
        projectTaskService.deleteProjectTaskByProjectSequence(backlogId, projectTaskId, principal.getName());

        return new ResponseEntity<>("Project task " + projectTaskId.toUpperCase() + " was deleted successfully", HttpStatus.OK);
    }

}
