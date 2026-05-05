package io.github.godoyjoao.workspace.workspace;

import io.github.godoyjoao.workspace.identity.CustomUserDetails;
import io.github.godoyjoao.workspace.workspace.dto.CreateWorkspace;
import io.github.godoyjoao.workspace.workspace.dto.WorkspaceView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping("/new")
    public ResponseEntity<?> newWorkspace(@RequestBody CreateWorkspace request, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        workspaceService.create(request, user.getIdUser());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<WorkspaceView>> getUserWorkspaces(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        List<Workspace> workspaces = workspaceService.findWorkspacesByUser(userDetails.getIdUser());

        return ResponseEntity.ok(workspaces.stream().map(Workspace::getView).toList());
    }

    @GetMapping("/{workspace}")
    public ResponseEntity<Workspace> getWorkspace(@PathVariable("workspace") UUID idWorkspace, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Workspace workspace = workspaceService.findWorkspace(idWorkspace, userDetails.getIdUser());

        return ResponseEntity.ok(workspace);
    }
}
