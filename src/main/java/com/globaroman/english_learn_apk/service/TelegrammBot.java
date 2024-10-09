package com.globaroman.english_learn_apk.service;


import com.globaroman.english_learn_apk.config.BotConfig;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegrammBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    //private final ModelPokerService modelPokerService;

    public TelegrammBot(BotConfig botConfig
                        ) {
        this.botConfig = botConfig;


        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "get a welcome message"));
        listofCommands.add(new BotCommand("/mydata", "get your data stored"));
        listofCommands.add(new BotCommand("/deletedata", "delete my data"));
        listofCommands.add(new BotCommand("/settings", "set your preferences"));
        listofCommands.add(new BotCommand("/load", "load info pair cards from file"));

        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Error setting bot's command list: ", e);
        }
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            Long chatId = update.getMessage().getChatId();

            switch (message) {
                case "/start" -> {

                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                }
                
                case "/load" -> {
                    //modelPokerService.downloadInfoFromFile();
                }
                case "/deletedata" -> {

                    //modelPokerService.deleteInfoFromDb();
                }
                default -> {
//                    Optional<ModelPoker> modelPoker = modelPokerService.getInfoFromDb(message);
//                    if (modelPoker.isEmpty()) {
//                        sendMessage(chatId, "Sorry, this command is not yet supported.");
//                    } else {
//                        ModelPoker poker = modelPoker.get();
//                        sendMessage(chatId, poker.getPair() + ": group: "
//                                + poker.getGroupCards().toUpperCase()
//                                + "\nstart: " + poker.getStartPos().toUpperCase() + "\nmid: "
//                                + poker.getMidPos().toUpperCase()
//                                + "\nbutton: " + poker.getButtPos().toUpperCase() + "\nBB or SB: "
//                                + poker.getBbSbPos().toUpperCase());
//                    }
                }
            }
        }
    }

    private void startCommandReceived(Long chatId, String firstName) {

        String answer = EmojiParser.parseToUnicode("Hi, " + firstName
                + ", nice to meet you!" + " :blush:");
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        sm.setText(textToSend);

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can not send message to " + chatId, e);
        }
    }

}
