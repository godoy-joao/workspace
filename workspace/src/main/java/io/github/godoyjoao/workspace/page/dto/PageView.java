package io.github.godoyjoao.workspace.page.dto;

import java.util.UUID;

public record PageView(
        UUID idPage,
        String title,
        Boolean hasChildren
) {
}
