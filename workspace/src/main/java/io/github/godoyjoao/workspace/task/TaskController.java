package io.github.godoyjoao.workspace.task;

import io.github.godoyjoao.workspace.exception.TenantViolationException;
import io.github.godoyjoao.workspace.identity.CustomUserDetails;
import io.github.godoyjoao.workspace.task.dto.CreateTask;
import io.github.godoyjoao.workspace.task.dto.TaskModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workspace/{workspace}/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskModel>> getTasks(@PathVariable("workspace") String workspace, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        List<TaskModel> tasks = taskService.findTasks(UUID.fromString(workspace), customUserDetails.getIdUser()).stream().map(Task::getModel).toList();

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{task}")
    public ResponseEntity<TaskModel> getTask(@PathVariable("workspace") String workspace, @PathVariable("task") String task, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Task _task = taskService.findTask(UUID.fromString(workspace), UUID.fromString(task), customUserDetails.getIdUser());

        return ResponseEntity.ok(_task.getModel());
    }

    @PostMapping("/new")
    public ResponseEntity<Void> createTask(@PathVariable("workspace") String workspace, @RequestBody CreateTask createTask, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        if (!workspace.equals(createTask.idWorkspace().toString()))
            throw new TenantViolationException("Invalid workspace.");

        taskService.createTask(createTask, customUserDetails.getIdUser());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{task}/update-status")
    public ResponseEntity<Void> updateTaskStatus(@PathVariable("workspace") String workspace, @PathVariable("task") String task, @RequestBody String status, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        TaskStatus taskStatus = TaskStatus.valueOf(status);

        taskService.updateTaskStatus(UUID.fromString(task), UUID.fromString(workspace), customUserDetails.getIdUser(), taskStatus);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{task}/archive")
    public ResponseEntity<Void> archiveTask(@PathVariable("workspace") String workspace, @PathVariable("task") String task, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        taskService.archiveTask(UUID.fromString(task), UUID.fromString(workspace), customUserDetails.getIdUser());

        return ResponseEntity.ok().build();
    }


}
