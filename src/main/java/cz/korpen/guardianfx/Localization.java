package cz.korpen.guardianfx;

import java.util.Locale;
import java.util.ResourceBundle;

public class Localization {

    // Singleton instance
    private static Localization instance;

    // ResourceBundle for storing language resources
    private ResourceBundle resourceBundle;

    // Private constructor to prevent instantiation
    private Localization() {
        // Default to English locale
        setLocale(Locale.getDefault());
    }

    // Method to get the Singleton instance
    public static Localization getInstance() {
        if (instance == null) {
            instance = new Localization();
        }
        return instance;
    }

    // Method to set the locale and load the corresponding ResourceBundle
    public void setLocale(Locale locale) {
        this.resourceBundle = ResourceBundle.getBundle("messages", locale);
    }

    // Method to get a translated string from the ResourceBundle
    public String getString(String key) {
        return resourceBundle.getString(key);
    }
}
