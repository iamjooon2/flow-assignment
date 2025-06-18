package flow.assignment.domain.repository;

import java.util.List;

import flow.assignment.domain.ExtensionType;
import flow.assignment.domain.FileExtension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileExtensionRepository extends JpaRepository<FileExtension, Long> {

    List<FileExtension> findByType(ExtensionType type);

    boolean existsByName(String name);
}
