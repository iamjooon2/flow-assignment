package flow.assignment;

import java.util.List;

import flow.assignment.domain.FileExtension;
import flow.assignment.domain.repository.FileExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DefaultFixedFileExtensionLoader implements CommandLineRunner {

    private final FileExtensionRepository fileExtensionRepository;

    @Override
    public void run(String... args) {
        fileExtensionRepository.saveAll(List.of(
                FileExtension.createFixed("bat", false),
                FileExtension.createFixed("cmd", false),
                FileExtension.createFixed("com", false),
                FileExtension.createFixed("cpi", false),
                FileExtension.createFixed("exe", false),
                FileExtension.createFixed("scr", false),
                FileExtension.createFixed("js", false)
        ));
    }
}
