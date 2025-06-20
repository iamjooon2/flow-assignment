package flow.assignment.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FileNameTest {

    @Test
    void 파일이름이_20자를_초과하면_예외가_발생한다() {
        // given
        var invalidName = "a".repeat(21);

        // when, then
        assertThatThrownBy(() -> FileName.create(invalidName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("확장자명은 20자를 초과할 수 없습니다");
    }

    @EmptySource
    @ParameterizedTest(name = "파일이름이 공백이면 예외가 발생한다={0}")
    void 파일이름이_공백이면_예외가_발생한다(String invalidName) {
        // given

        // when, then
        assertThatThrownBy(() -> FileName.create(invalidName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("파일 이름은 공백이 될 수 없습니다");
    }

    @ValueSource(strings = {"한글이름", "CAPITAL_LETTER", "12345", "....."})
    @ParameterizedTest(name = "확장자명은 영어 소문자만 가능하다 ={0}")
    void 확장자명은_영어_소문자만_가능하다(String invalidName) {
        // given

        // when, then
        assertThatThrownBy(() -> FileName.create(invalidName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("확장자명은 영어 소문자만 사용 가능합니다");
    }

    @ValueSource(strings = {".exe", ".bat", ".cmd"})
    @ParameterizedTest(name = "확장자명이 온점으로 시작될 경우 제거된다 ={0}")
    void 확장자명이_온점으로_시작할_경우_제거된다(String startsWithDotName) {
        // given
        var replaced = startsWithDotName.substring(1);

        // when
        var fileName = FileName.create(startsWithDotName);

        // then
        var value = fileName.getValue();
        assertAll(
                () -> assertThat(value).doesNotStartWith("."),
                () -> assertThat(value).isEqualTo(replaced)
        );
    }

    @Test
    void 같은_값이면_동등하다() {
        // given
        var value1 = "abc";
        var value2 = "abc";

        // when
        var fileName1 = FileName.create(value1);
        var fileName2 = FileName.create(value2);

        // then
        assertThat(fileName1).isEqualTo(fileName2);
    }
}
