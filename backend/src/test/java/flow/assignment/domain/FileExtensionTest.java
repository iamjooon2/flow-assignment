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
        var invalidName = "0".repeat(21);

        // when, then
        assertThatThrownBy(() -> FileExtension.createCustom(invalidName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("확장자명은 20자를 초과할 수 없습니다");
    }

    @Test
    void 커스텀_확장자는_체크상태를_변경할_수_없다() {
        // given
        var fileExtension = FileExtension.createCustom("test");

        // when, then
        assertThatThrownBy(fileExtension::toChecked)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("커스텀 확장자는 체크상태를 조절할 수 없습니다");
    }
}
