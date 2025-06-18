package flow.assignment.dto.response;

import flow.assignment.domain.FileExtension;

public record CustomFileCreateResponse(
        Long id,
        String name
) {

    public static CustomFileCreateResponse of(FileExtension fileExtension) {
        return new CustomFileCreateResponse(fileExtension.getId(), fileExtension.getName());
    }

}
