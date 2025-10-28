package org.gycoding.heraldsofchaos.shared;

public class IdentifierGenerator {
    public static String generate(String name) {
        return name.toLowerCase()
                .replace(" ", "_")
                .replace("ä", "a")
                .replace("ë", "e")
                .replace("ï", "i")
                .replace("ö", "o")
                .replace("ü", "u")
                .replace("æ", "ae")
                .replace("ø", "o")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ý", "y");
    }
}
