package flow.assignment.domain;

import java.util.Objects;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FileName {

    private static final String DOT = ".";
    private static final int MAX_NAME_LENGTH = 20;
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("^[a-z]+$");

    private String value;

    private FileName(String value) {
        this.value = value;
    }

    public static FileName create(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("파일 이름은 공백이 될 수 없습니다");
        }
        value = normalize(value);
        validate(value);
        return new FileName(value);
    }

    private static String normalize(String value) {
        String lowerCased = value.trim().toLowerCase();
        if (lowerCased.startsWith(DOT)) {
            return lowerCased.substring(1);
        }
        return value;
    }

    private static void validate(String value) {
        if (value.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("확장자명은 " + MAX_NAME_LENGTH + "자를 초과할 수 없습니다");
        }
        if (!LOWERCASE_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("확장자명은 영어 소문자만 사용 가능합니다");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final FileName fileName = (FileName) o;
        return Objects.equals(value, fileName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
