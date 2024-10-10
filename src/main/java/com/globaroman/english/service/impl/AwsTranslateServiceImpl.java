package com.globaroman.english.service.impl;

import com.globaroman.english.service.AwsTranslateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;

@Service
public class AwsTranslateServiceImpl implements AwsTranslateService {
    @Value("${AWS_ACCESS_KEY}")
    private String accessKey;

    @Value("${AWS_SECRET_KEY}")
    private String secretKey;

    @Override
    public String translateText(String text, String sourceLang, String targetLang) {

        TranslateTextRequest request = TranslateTextRequest.builder()
                .sourceLanguageCode(sourceLang) // Наприклад, "en" для англійської
                .targetLanguageCode(targetLang) // Наприклад, "uk" для української
                .text(text)
                .build();
        TranslateTextResponse response = getClient().translateText(request);
        return response.translatedText();
    }

    private TranslateClient getClient() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        return TranslateClient.builder()
                .region(Region.US_EAST_1) // Вкажіть потрібний регіон
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
}
