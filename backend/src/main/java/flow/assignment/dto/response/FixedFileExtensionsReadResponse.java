package flow.assignment.dto.response;

import java.util.List;

import flow.assignment.domain.FileExtension;
import lombok.Getter;

public record FixedFileExtensionsReadResponse(
        List<FileExtensionReadResponse> data
) {

    public static FixedFileExtensionsReadResponse of(List<FileExtension> fileExtensions) {
        List<FileExtensionReadResponse> data = fileExtensions.stream()
                .map(fileExtension -> new FileExtensionReadResponse(fileExtension.getId(), fileExtension.isChecked(), fileExtension.getName()))
                .toList();
        return new FixedFileExtensionsReadResponse(data);
    }

    @Getter
    public static class FileExtensionReadResponse {

        private final Long id;
        private final String name;
        private final boolean isChecked;

        public FileExtensionReadResponse(Long id, boolean isChecked, String name) {
            this.id = id;
            this.isChecked = isChecked;
            this.name = name;
        }
    }
}
