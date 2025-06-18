package flow.assignment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static flow.assignment.domain.ExtensionType.CUSTOM;
import static flow.assignment.domain.ExtensionType.FIXED;

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

    private FileExtension(String name, ExtensionType type, boolean isChecked) {
        if (name.length() > MAX_NAME_NAME) {
            throw new IllegalArgumentException("확장자명은 20자를 초과할 수 없습니다");
        }
        this.name = name;
        this.type = type;
        this.isChecked = isChecked;
    }

    public static FileExtension createFixed(String name, boolean isChecked) {
        return new FileExtension(name, FIXED, isChecked);
    }

    public static FileExtension createCustom(String name) {
        return new FileExtension(name, CUSTOM, false);
    }

    public void toChecked() {
        validateCheckable();
        this.isChecked = true;
    }

    public void toUnchecked() {
        validateCheckable();
        this.isChecked = false;
    }

    private void validateCheckable() {
        if (type == CUSTOM) {
            throw new IllegalArgumentException("커스텀 확장자는 체크상태를 조절할 수 없습니다");
        }
    }
}
