package flow.assignment.dto.response;

import java.util.List;

import flow.assignment.domain.FileExtension;
import lombok.Getter;

public record CustomFileExtensionsReadResponse(
        long maxCount,
        long totalCount,
        List<CustomExtensionReadResponse> data
) {

    public static CustomFileExtensionsReadResponse of(long totalCount,  long maxCount, List<FileExtension> fileExtensions) {
        List<CustomExtensionReadResponse> data = fileExtensions.stream()
                .map(fileExtension -> new CustomExtensionReadResponse(fileExtension.getId(), fileExtension.getName()))
                .toList();
        return new CustomFileExtensionsReadResponse(maxCount, totalCount, data);
    }

    @Getter
    static class CustomExtensionReadResponse {

        private final Long id;
        private final String name;

        public CustomExtensionReadResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
