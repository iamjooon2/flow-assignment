package flow.assignment.dto.request;

import jakarta.validation.constraints.NotNull;

public record FixedFileExtensionUpdateRequest(
        @NotNull(message = "체크 여부가 입력되지 않았습니다")
        Boolean isChecked
) {

}
