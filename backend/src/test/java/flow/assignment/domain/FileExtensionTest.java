package flow.assignment.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FileExtensionTest {

    @Test
    void 파일_이름이_20자를_초과한다면_예외가_발생한다() {
        // given
        String invalidName = "0".repeat(21);

        // when, then
        assertThatThrownBy(() -> new FileExtension(invalidName, ExtensionType.CUSTOM, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("확장자명은 20자를 초과할 수 없습니다");
    }
}
