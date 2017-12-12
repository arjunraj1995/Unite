//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dbz.com.unite;

import com.memetix.mst.MicrosoftTranslatorAPI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

//import dbz.com.rmtheis.yandtran.language.LanguageVoice;

public enum LanguageVoice {
    ARABIC("ar"),
    BULGARIAN("bg"),
    CATALAN("ca"),
    CHINESE("zh"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    ENGLISH("en"),
    ESTONIAN("et"),
    FINNISH("fi"),
    FRENCH("fr"),
    GERMAN("de"),
    GREEK("el"),
    HINDI("hi"),
    HUNGARIAN("hu"),
    INDONESIAN("id"),
    ITALIAN("it"),
    JAPANESE("ja"),
    KOREAN("ko"),
    LATVIAN("lv"),
    LITHUANIAN("lt"),
    NORWEGIAN("no"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SLOVAK("sk"),
    SLOVENIAN("sl"),
    SPANISH("es"),
    SWEDISH("sv"),
    THAI("th"),
    TURKISH("tr"),
    UKRAINIAN("uk"),
    VIETNAMESE("vi");

    /**
     * String representation of this language.
     */
    private final String language;

    /**
     * Enum constructor.
     * @param pLanguage The language identifier.
     */
    private LanguageVoice(final String pLanguage) {
        language = pLanguage;
    }

    public static LanguageVoice fromString(final String pLanguage) {
        for (LanguageVoice l : values()) {
            if (l.toString().equals(pLanguage)) {
                return l;
            }
        }
        return null;
    }

    /**
     * Returns the String representation of this language.
     * @return The String representation of this language.
     */
    @Override
    public String toString() {
        return language;
    }

}