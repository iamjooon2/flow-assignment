package flow.assignment.application;

import java.util.List;

import flow.assignment.domain.FileExtension;
import flow.assignment.domain.repository.FileExtensionRepository;
import flow.assignment.dto.request.FileExtensionCreateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import static flow.assignment.domain.ExtensionType.CUSTOM;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class FileExtensionServiceTest {

    @Autowired
    FileExtensionRepository fileExtensionRepository;

    @Autowired
    FileExtensionService sut;

    @Nested
    class 조회 {

        @Test
        void 고정_확장자_목록을_조회할_수_있다() {
            // given
            fileExtensionRepository.saveAll(List.of(
                    FileExtension.createFixed("bat", false),
                    FileExtension.createFixed("cmd", false),
                    FileExtension.createFixed("com", false),
                    FileExtension.createFixed("cpi", false),
                    FileExtension.createFixed("exe", false),
                    FileExtension.createFixed("scr", false),
                    FileExtension.createFixed("js", false)
            ));

            // when
            var customFileExtensionsReadResponse = sut.readFixedExtensions();

            // then
            var data = customFileExtensionsReadResponse.data();
            assertAll(
                    () -> assertThat(data.size()).isEqualTo(7),
                    () -> assertThat(data.get(0).getName()).isEqualTo("bat"),
                    () -> assertThat(data.get(0).isChecked()).isFalse(),
                    () -> assertThat(data.get(1).getName()).isEqualTo("cmd"),
                    () -> assertThat(data.get(1).isChecked()).isFalse(),
                    () -> assertThat(data.get(2).getName()).isEqualTo("com"),
                    () -> assertThat(data.get(2).isChecked()).isFalse(),
                    () -> assertThat(data.get(3).getName()).isEqualTo("cpi"),
                    () -> assertThat(data.get(3).isChecked()).isFalse(),
                    () -> assertThat(data.get(4).getName()).isEqualTo("exe"),
                    () -> assertThat(data.get(4).isChecked()).isFalse(),
                    () -> assertThat(data.get(5).getName()).isEqualTo("scr"),
                    () -> assertThat(data.get(5).isChecked()).isFalse(),
                    () -> assertThat(data.get(6).getName()).isEqualTo("js"),
                    () -> assertThat(data.get(6).isChecked()).isFalse()
            );
        }

        @Test
        void 커스텀_확장자_목록을_조회할_수_있다() {
            // given
            fileExtensionRepository.save(FileExtension.createFixed("test1", false));
            fileExtensionRepository.save(FileExtension.createCustom("test2"));
            fileExtensionRepository.save(FileExtension.createCustom("test3"));
            fileExtensionRepository.save(FileExtension.createCustom("test4"));

            // when
            PageRequest pageRequest = PageRequest.of(0, 10);
            var customFileExtensionsReadResponse = sut.readCustomExtensions(pageRequest);

            // then
            assertAll(
                    () -> assertThat(customFileExtensionsReadResponse.currentCount()).isEqualTo(3),
                    () -> assertThat(customFileExtensionsReadResponse.maxCount()).isEqualTo(200)
            );
        }
    }

    @Nested
    class 추가 {

        @Test
        void 커스텀_확장자를_추가할_수_있다() {
            // given
            var request = new FileExtensionCreateRequest("sh");

            // when
            var response = sut.createCustomFileExtension(request);

            // then
            var actual = fileExtensionRepository.findById(response.id()).orElseThrow();
            assertAll(
                    () -> assertThat(actual.getName()).isEqualTo("sh"),
                    () -> assertThat(actual.getType()).isEqualTo(CUSTOM),
                    () -> assertThat(actual.isChecked()).isFalse()
            );
        }

        @Test
        void 커스텀_확장자_추가시_20자가_넘어간다면_생성할수_없고_예외가_발생한다() {
            // given
            var invalidName = "0".repeat(21);
            var request = new FileExtensionCreateRequest(invalidName);

            // when, then
            assertThatThrownBy(() -> sut.createCustomFileExtension(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("확장자명은 20자를 초과할 수 없습니다");
        }

        @Test
        void 커스텀_확장자를_200개넘게_등록하면_예외가_발생한다() {
            // given
            for (int i = 1; i <= 200; i += 1) {
                fileExtensionRepository.save(FileExtension.createCustom("name" + i));
            }
            var request = new FileExtensionCreateRequest("name201");

            // when, then
            assertThatThrownBy(() -> sut.createCustomFileExtension(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("커스텀확장자는 최대 200개까지만 등록 가능합니다");
        }

        @Test
        void 이미_등록된_확장자명을_등록시_예외가_발생한다() {
            // given
            var duplicatedName = "sh";
            fileExtensionRepository.save(FileExtension.createCustom(duplicatedName));

            var request = new FileExtensionCreateRequest(duplicatedName);

            // when, then
            assertThatThrownBy(() -> sut.createCustomFileExtension(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이미 등록된 확장자명입니다");
        }
    }

    @Nested
    class 수정 {

        @Test
        void 고정_확장자의_체크상태를_체크에서_비체크로_변경할_수_있다() {
            // given
            var fileExtension = fileExtensionRepository.save(FileExtension.createFixed("test1", false));

            // when
            sut.updateFixedExtensionCheckStatus(fileExtension.getId(), true);

            // then
            var actual = fileExtensionRepository.findById(fileExtension.getId()).orElseThrow();
            assertThat(actual.isChecked()).isTrue();
        }

        @Test
        void 고정_확장자의_체크상태를_비체크에서_체크로_변경할_수_있다() {
            // given
            var fileExtension = fileExtensionRepository.save(FileExtension.createFixed("bat", true));

            // when
            sut.updateFixedExtensionCheckStatus(fileExtension.getId(), false);

            // then
            var actual = fileExtensionRepository.findById(fileExtension.getId()).orElseThrow();
            assertThat(actual.isChecked()).isFalse();
        }

        @Test
        void 존재하지_않는_고정_확장자의_체크상태를_변경할시_예외가_발생한다() {
            // given
            var nonExistingId = -1L;

            // when, then
            assertThatThrownBy(() -> sut.updateFixedExtensionCheckStatus(nonExistingId, true))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("존재하지 않는 확장자 id입니다");
        }
    }

    @Test
    void 커스텀_확장자를_삭제할_수_있다() {
        // given
        var fileExtension = fileExtensionRepository.save(FileExtension.createCustom("bat"));

        // when
        sut.delete(fileExtension.getId());

        // then
        var actual = fileExtensionRepository.findById(fileExtension.getId());
        assertThat(actual).isEmpty();
    }

    @AfterEach
    void setUp() {
        fileExtensionRepository.deleteAll();
    }
}
