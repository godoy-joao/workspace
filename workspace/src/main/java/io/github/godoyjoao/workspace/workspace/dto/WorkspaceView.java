package io.github.godoyjoao.workspace.workspace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class WorkspaceView {
    private UUID idWorkspace;
    private String name;
}
