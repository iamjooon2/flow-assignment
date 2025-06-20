package flow.assignment;

import java.util.List;

import flow.assignment.domain.FileExtension;
import flow.assignment.domain.repository.FileExtensionRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static flow.assignment.domain.ExtensionType.FIXED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class DefaultFixedFileExtensionLoaderTest {

    @Autowired
    private FileExtensionRepository fileExtensionRepository;

    @Test
    void 컨텍스트_초기화후_자동으로_쓰는_고정_확장자_목록을_조회할_수_있다() {
        // when
        List<FileExtension> fixedExtensions = fileExtensionRepository.findByType(FIXED);

        // then
        List<String> names = fixedExtensions.stream()
                .map(fileExtension -> fileExtension.getName().getValue())
                .toList();
        assertThat(names).isEqualTo(List.of("bat", "cmd", "com", "cpi", "exe", "scr", "js"));
        assertThat(names.size()).isEqualTo(7);
    }
}
