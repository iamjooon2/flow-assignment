package flow.assignment.dto.request;

import flow.assignment.domain.FileExtension;
import jakarta.validation.constraints.NotEmpty;


public record FileExtensionCreateRequest(
        @NotEmpty(message = "확장자명이 입력되지 않았습니다")
        String name
) {

    public FileExtension toEntity() {
        return FileExtension.createCustom(name);
    }
}
