import java.util.EnumSet;

public enum Language {
    FR("fr", "🇫🇷"),
    EN("en", "🇬🇸"),
    DEFAULT("nope", "🌐");

    private final String code;
    private final String flag;

    Language(String code, String flag) {
        this.code = code;
        this.flag = flag;
    }

    public String code() {
        return code;
    }

    public String flag() {
        return flag;
    }

    public static Language fromCode(String code) {
        for (Language language : values()) {
            if (language.code.equalsIgnoreCase(code)) {
                return language;
            }
        }
        return DEFAULT;
    }
}
