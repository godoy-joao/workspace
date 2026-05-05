package io.github.godoyjoao.workspace.page;

import io.github.godoyjoao.workspace.identity.CustomUserDetails;
import io.github.godoyjoao.workspace.page.dto.CreatePage;
import io.github.godoyjoao.workspace.page.dto.PageModel;
import io.github.godoyjoao.workspace.page.dto.PageView;
import io.github.godoyjoao.workspace.page.dto.UpdatePage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workspace/{workspace}/page")
@RequiredArgsConstructor
@Slf4j
public class PageController {

    private final PageService pageService;

    @PostMapping("/new")
    public ResponseEntity<Void> createPage(@PathVariable("workspace") String workspace, @RequestBody CreatePage createPage, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        pageService.createPage(createPage, UUID.fromString(workspace), userDetails.getIdUser());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{page}/children")
    public ResponseEntity<List<PageView>> getPageChildren(@PathVariable("workspace") String workspace, @PathVariable("page") String page, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        List<PageView> views = pageService.findChildrenView(UUID.fromString(page), UUID.fromString(workspace), userDetails.getIdUser());
        log.info("Fetched {} children for page {} in workspace {}", views.size(), page, workspace);
        return ResponseEntity.ok(views);
    }

    @GetMapping("/")
    public ResponseEntity<List<PageView>> getPageViews(@PathVariable("workspace") String workspace, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        List<PageView> views = pageService.findPageViews(UUID.fromString(workspace), userDetails.getIdUser());
        log.info("Fetched {} pages for workspace {}", views.size(), workspace);
        return ResponseEntity.ok(views);
    }

    @GetMapping("/{page}")
    public ResponseEntity<PageModel> getPage(@PathVariable("workspace") String workspace, @PathVariable("page") String page, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Page fetchedPage = pageService.findPage(UUID.fromString(page), UUID.fromString(workspace), userDetails.getIdUser());

        if (fetchedPage == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(fetchedPage.getModel());
    }

    @PutMapping("/{page}/update")
    public ResponseEntity<Void> updatePage(@PathVariable("workspace") String workspace, @PathVariable("page") String page, @RequestBody UpdatePage updatePage, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        pageService.updatePage(UUID.fromString(page), UUID.fromString(workspace), userDetails.getIdUser(), updatePage);

        return ResponseEntity.ok().build();
    }

}
