package flow.assignment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Entity
public class FileExtension {

    private static final int MAX_NAME_NAME = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ExtensionType type;

    private boolean isChecked;

    public FileExtension(String name, ExtensionType type, boolean isChecked) {
        if (name.length() > MAX_NAME_NAME) {
            throw new IllegalArgumentException("확장자명은 20자를 초과할 수 없습니다");
        }
        this.name = name;
        this.type = type;
        this.isChecked = isChecked;
    }
}
