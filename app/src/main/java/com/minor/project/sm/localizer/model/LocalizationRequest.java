package com.minor.project.sm.localizer.model;

public class LocalizationRequest {
    private String textToTranslate;
    private String selectedLanguage;
    private String selectedLanguageKey;

    public String getTextToTranslate() {
        return textToTranslate;
    }

    public void setTextToTranslate(String textToTranslate) {
        this.textToTranslate = textToTranslate;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public String getSelectedLanguageKey() {
        return selectedLanguageKey;
    }

    public void setSelectedLanguageKey(String selectedLanguageKey) {
        this.selectedLanguageKey = selectedLanguageKey;
    }
}
