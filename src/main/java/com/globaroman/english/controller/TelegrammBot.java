package com.globaroman.english.controller;

import com.globaroman.english.config.BotConfig;
import com.globaroman.english.service.AwsTranslateService;
import com.globaroman.english.service.DictionaryService;
import com.globaroman.english.service.ProcessingDataService;
import com.globaroman.english.service.ReadWriteFromToFileService;
import com.vdurmont.emoji.EmojiParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegrammBot extends TelegramLongPollingBot {
    private static final int FIVE_WORDS = 5;
    private static final int THREE_WORDS = 3;
    private static final int TWO_WORDS = 2;
    private static final int ONE_WORD = 1;
    private final BotConfig botConfig;
    private final AwsTranslateService translateService;
    private final DictionaryService dictionaryService;
    private final ReadWriteFromToFileService readWriteFromToFileService;
    private final ProcessingDataService processingDataService;

    public TelegrammBot(BotConfig botConfig,
                        AwsTranslateService translateService,
                        DictionaryService dictionaryService,
                        ReadWriteFromToFileService readWriteFromToFileService,
                        ProcessingDataService processingDataService
                        ) {
        this.botConfig = botConfig;
        this.translateService = translateService;
        this.dictionaryService = dictionaryService;
        this.readWriteFromToFileService = readWriteFromToFileService;
        this.processingDataService = processingDataService;

        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "get a welcome message"));
        listofCommands.add(new BotCommand("/five", "get 5 random english words"));
        listofCommands.add(new BotCommand("/three", "get 3 random english words"));
        listofCommands.add(new BotCommand("/two", "get 2 random english words"));
        listofCommands.add(new BotCommand("/one", "get random english word"));
        listofCommands.add(new BotCommand("/count", "кількість нових слів в базі даних"));

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

                case "/five" -> {
                    sendMessage(chatId, getRandomWordFromDB(FIVE_WORDS));
                }

                case "/three" -> {
                    sendMessage(chatId, getRandomWordFromDB(THREE_WORDS));
                }

                case "/two" -> {
                    sendMessage(chatId, getRandomWordFromDB(TWO_WORDS));
                }

                case "/one" -> {
                    sendMessage(chatId, getRandomWordFromDB(ONE_WORD));
                }

                case "/count" -> {
                    sendMessage(chatId, dictionaryService.countNewWords());
                }

                default -> {
                    sendMessage(chatId, addNewWordsToDB(message));

                }
            }
        }

        if (update.hasMessage() && update.getMessage().hasDocument()) {
            Document document = update.getMessage().getDocument();
            String fileId = document.getFileId();
            Long chatId = update.getMessage().getChatId();

            try {

                GetFile getFileMethod = new GetFile();
                getFileMethod.setFileId(fileId);
                File telegramFile = execute(getFileMethod);

                String fileUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + telegramFile.getFilePath();

                List<String> dataFromFiles = readWriteFromToFileService.readDataFromFile(fileUrl);

                Set<String> afterProcessDatas = processingDataService
                        .getUniqueDataAfterProcess(dataFromFiles);

                sendMessage(chatId, dictionaryService.loadNewWordToDataBase(afterProcessDatas));

            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String addNewWordsToDB(String message) {
        int count = 0;
        StringBuilder sb = new StringBuilder();

        String[] strings = message.split("\n");
        for (String line : strings) {
            sb.append(dictionaryService.saveNewWord(line)).append("\n");
        }
        return sb.toString();
    }

    private String getRandomWordFromDB(int countWords) {
        return "Добавлено:\n" + dictionaryService.getWordsFromDB(countWords);
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
