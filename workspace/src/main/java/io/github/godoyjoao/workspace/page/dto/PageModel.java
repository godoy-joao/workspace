package io.github.godoyjoao.workspace.page.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageModel {

    private UUID idPage;
    private UUID idParent;
    private UUID idCreator;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
