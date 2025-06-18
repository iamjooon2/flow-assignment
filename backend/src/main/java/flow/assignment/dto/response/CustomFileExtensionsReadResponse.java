package flow.assignment.dto.response;

import java.util.List;

import flow.assignment.domain.FileExtension;

public record CustomFileExtensionsReadResponse(
        long maxCount,
        long currentCount,
        List<CustomExtensionReadResponse> data
) {

    public static CustomFileExtensionsReadResponse of(List<FileExtension> fileExtensions, long maxCount) {
        List<CustomExtensionReadResponse> data = fileExtensions.stream()
                .map(fileExtension -> new CustomExtensionReadResponse(fileExtension.getId(), fileExtension.getName()))
                .toList();
        return new CustomFileExtensionsReadResponse(maxCount, fileExtensions.size(), data);
    }

    static class CustomExtensionReadResponse {

        private final Long id;
        private final String name;

        public CustomExtensionReadResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
