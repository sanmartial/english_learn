package com.globaroman.english_learn_apk.service;


import com.globaroman.english_learn_apk.config.BotConfig;
import com.globaroman.english_learn_apk.dto.DataFromDataBaseDto;
import com.globaroman.english_learn_apk.model.DictionaryWord;
import com.vdurmont.emoji.EmojiParser;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TelegrammBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final LoadWorldFromDataBase loadWorldFromDataBase;
    private final AwsTranslateService translateService;
    private final DictionaryService dictionaryService;
    //private final ModelPokerService modelPokerService;

    public TelegrammBot(BotConfig botConfig,
                        LoadWorldFromDataBase loadWorldFromDataBase,
                        AwsTranslateService translateService,
                        DictionaryService dictionaryService
                        ) {
        this.botConfig = botConfig;
        this.loadWorldFromDataBase = loadWorldFromDataBase;
        this.translateService = translateService;
        this.dictionaryService = dictionaryService;

        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "get a welcome message"));
        listofCommands.add(new BotCommand("/mydata", "get your data stored"));
        listofCommands.add(new BotCommand("/deletedata", "delete my data"));
        listofCommands.add(new BotCommand("/settings", "set your preferences"));
        listofCommands.add(new BotCommand("/load", "load data from DB"));

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
                    List<DictionaryWord> list = new ArrayList<>();
                    for (DataFromDataBaseDto dto : loadWorldFromDataBase.getDate()) {
                        if (!dto.getWord().isEmpty()) {
                            DictionaryWord word = new DictionaryWord();
                            word.setEnglishWord(dto.getWord());
                            word.setTranslatedWord(translateService.translateText(dto.getWord(), "en", "ru"));
                            word.setWordLearned(true);
                            System.out.println(word);
                            list.add(word);
                        }
                    }

                    dictionaryService.loadToDataBase(list);

                }
                case "/deletedata" -> {

                    //modelPokerService.deleteInfoFromDb();
                }
                default -> {

                    System.out.println(message);
                    //System.out.println(translateService.translateText(message, "en", "ru"));

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

        if (update.hasMessage() && update.getMessage().hasDocument()) {
            Document document = update.getMessage().getDocument();
            String fileId = document.getFileId();

            try {
                GetFile getFileMethod = new GetFile();
                getFileMethod.setFileId(fileId);
                File telegramFile = execute(getFileMethod);

                String fileUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + telegramFile.getFilePath();

                URL url = new URL(fileUrl);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = null;
                List<String> list = new ArrayList<>();

                while ((line = in.readLine()) != null) {
                    list.add(line);
                }
                in.close();

                for (String string : list) {
                    System.out.println(string);
                }

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (TelegramApiException | IOException e) {
                throw new RuntimeException(e);
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
