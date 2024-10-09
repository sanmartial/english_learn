package com.globaroman.english_learn_apk.config;

import com.globaroman.english_learn_apk.service.TelegrammBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
public class BotInitializer {

    private final TelegrammBot telegrammBot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(telegrammBot);

        } catch (TelegramApiException e) {
            throw new RuntimeException("Can not init telegram bot "
                    + telegrammBot.getBotUsername(), e);
        }
    }
}
