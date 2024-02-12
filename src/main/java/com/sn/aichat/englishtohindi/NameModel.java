package com.sn.aichat.englishtohindi;

public class NameModel {
    private int id;
    private String words;
    private String meaning;
    private String type;
    private String hindiMeaning;
    private String englishMeaning;

    public NameModel() {
        // Default constructor
    }

    public NameModel(int id, String words, String meaning, String type, String hindiMeaning, String englishMeaning) {
        this.id = id;
        this.words = words;
        this.meaning = meaning;
        this.type = type;
        this.hindiMeaning = hindiMeaning;
        this.englishMeaning = englishMeaning;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHindiMeaning() {
        return hindiMeaning;
    }

    public void setHindiMeaning(String hindiMeaning) {
        this.hindiMeaning = hindiMeaning;
    }
    public void setwordid(String hindiMeaning) {
        this.hindiMeaning = hindiMeaning;
    }
    public String getEnglishMeaning() {
        return englishMeaning;
    }

    public void setEnglishMeaning(String englishMeaning) {
        this.englishMeaning = englishMeaning;
    }
}
