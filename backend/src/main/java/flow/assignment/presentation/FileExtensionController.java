package flow.assignment.presentation;

import flow.assignment.application.FileExtensionService;
import flow.assignment.dto.request.FileExtensionCreateRequest;
import flow.assignment.dto.request.FixedFileExtensionUpdateRequest;
import flow.assignment.dto.response.CustomFileCreateResponse;
import flow.assignment.dto.response.CustomFileExtensionsReadResponse;
import flow.assignment.dto.response.FixedFileExtensionsReadResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin(origins = "https://flow-assignment-front.vercel.app")
@RequestMapping("/file-extensions")
@RequiredArgsConstructor
@RestController
public class FileExtensionController {

    private final FileExtensionService fileExtensionService;

    @GetMapping("/fixed")
    public ResponseEntity<FixedFileExtensionsReadResponse> getFileExtensions() {
        FixedFileExtensionsReadResponse response = fileExtensionService.readFixedExtensions();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/custom")
    public ResponseEntity<CustomFileExtensionsReadResponse> getFileExtensionsCustom(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        CustomFileExtensionsReadResponse response = fileExtensionService.readCustomExtensions(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CustomFileCreateResponse> createFileExtension(
            @RequestBody @Valid FileExtensionCreateRequest request
    ) {
        CustomFileCreateResponse response = fileExtensionService.createCustomFileExtension(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateFileExtensionCheckStatus(
            @PathVariable Long id,
            @RequestBody @Valid FixedFileExtensionUpdateRequest request
    ) {
        fileExtensionService.updateFixedExtensionCheckStatus(id, request.isChecked());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFileExtension(@PathVariable Long id) {
        fileExtensionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
