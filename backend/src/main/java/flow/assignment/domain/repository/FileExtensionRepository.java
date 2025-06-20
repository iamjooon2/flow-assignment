package flow.assignment.domain.repository;

import java.util.List;

import flow.assignment.domain.ExtensionType;
import flow.assignment.domain.FileExtension;
import flow.assignment.domain.FileName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileExtensionRepository extends JpaRepository<FileExtension, Long> {

    Page<FileExtension> findByType(ExtensionType type, Pageable pageable);

    List<FileExtension> findByType(ExtensionType type);

    boolean existsByName(FileName name);
}
