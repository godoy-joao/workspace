package io.github.godoyjoao.workspace.page.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreatePage(
        @NotBlank String title,
        UUID idParent
) {
}
