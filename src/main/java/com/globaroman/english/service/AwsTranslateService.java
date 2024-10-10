package com.globaroman.english.service;

public interface AwsTranslateService {
    String translateText(String text, String sourceLang, String targetLang);
}
