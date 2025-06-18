package flow.assignment.application;

import java.util.List;

import flow.assignment.domain.FileExtension;
import flow.assignment.domain.repository.FileExtensionRepository;
import flow.assignment.dto.request.FileExtensionCreateRequest;
import flow.assignment.dto.response.CustomFileCreateResponse;
import flow.assignment.dto.response.CustomFileExtensionsReadResponse;
import flow.assignment.dto.response.FixedFileExtensionsReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static flow.assignment.domain.ExtensionType.CUSTOM;
import static flow.assignment.domain.ExtensionType.FIXED;

@RequiredArgsConstructor
@Transactional
@Service
public class FileExtensionService {

    private static final long FIXED_RATE_MAX_COUNT = 200;

    private final FileExtensionRepository fileExtensionRepository;

    @Transactional(readOnly = true)
    public FixedFileExtensionsReadResponse readFixedExtensions() {
        List<FileExtension> fileExtensions = fileExtensionRepository.findByType(FIXED);
        return FixedFileExtensionsReadResponse.of(fileExtensions);
    }

    @Transactional(readOnly = true)
    public CustomFileExtensionsReadResponse readCustomExtensions(Pageable pageable) {
        List<FileExtension> total = fileExtensionRepository.findByType(CUSTOM);
        Page<FileExtension> page = fileExtensionRepository.findByType(CUSTOM, pageable);
        List<FileExtension> content = page.getContent();
        return CustomFileExtensionsReadResponse.of(total.size(), FIXED_RATE_MAX_COUNT, content);
    }

    public CustomFileCreateResponse createCustomFileExtension(FileExtensionCreateRequest request) {
        List<FileExtension> fixedExtensions = fileExtensionRepository.findByType(CUSTOM);

        if (fixedExtensions.size() >= FIXED_RATE_MAX_COUNT) {
            throw new IllegalArgumentException("커스텀확장자는 최대 " + FIXED_RATE_MAX_COUNT + "개까지만 등록 가능합니다");
        }
        if (fileExtensionRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("이미 등록된 확장자명입니다.");
        }
        FileExtension fileExtension = fileExtensionRepository.save(request.toEntity());
        return CustomFileCreateResponse.of(fileExtension);
    }

    public void updateFixedExtensionCheckStatus(Long id, boolean isChecked) {
        FileExtension fileExtension = fileExtensionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 확장자 id입니다: " + id));

        if (isChecked) {
            fileExtension.toChecked();
        } else {
            fileExtension.toUnchecked();
        }
    }

    public void delete(Long id) {
        fileExtensionRepository.deleteById(id);
    }
}
