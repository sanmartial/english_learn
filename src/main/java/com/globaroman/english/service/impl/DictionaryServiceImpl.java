package com.globaroman.english.service.impl;

import com.globaroman.english.model.DictionaryWord;
import com.globaroman.english.repository.DictionaryRepository;
import com.globaroman.english.service.AwsTranslateService;
import com.globaroman.english.service.DictionaryService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final AwsTranslateService translateService;

    @Override
    public void loadToDataBase(List<DictionaryWord> list) {
        dictionaryRepository.saveAll(list);
    }

    @Override
    public String loadToDataBaseIfNoThisWord(List<String> list) {
        int count = 0;

        List<DictionaryWord> dictionaryWords = new ArrayList<>();

        for (String string : list) {
            List<DictionaryWord> words = dictionaryRepository.findByWord(string);

            for (DictionaryWord word : words) {
                word.setWordLearned(false);
                dictionaryRepository.save(word);
            }
//            if (!string.isEmpty() && words.isEmpty()) {
//
//
//                DictionaryWord dictWord = new DictionaryWord();
//                dictWord.setEnglishWord(string);
//                dictWord.setTranslatedWord(translateService.translateText(string,
//                        "en", "ru"));
//                dictWord.setWordLearned(false);
//                dictionaryRepository.save(dictWord);
            count++;
        }

        return "Добавлено " + count + " слів до бази даних";
    }

    @Override
    public String getRandomWordFromDB() {
        DictionaryWord word = dictionaryRepository.findRandomWord();
        word.setWordLearned(true);
        dictionaryRepository.save(word);
        return word.getEnglishWord() + " : " + word.getTranslatedWord();
    }

    @Override
    public String saveNewWord(String line) {
        List<DictionaryWord> words = dictionaryRepository.findByWord(line);
        if (words.isEmpty()) {
            DictionaryWord word = new DictionaryWord();
            word.setEnglishWord(line);
            word.setTranslatedWord(translateService.translateText(line,
                    "en", "ru"));
            word.setWordLearned(false);
            dictionaryRepository.save(word);
            return line + " > success";
        }

        return line + " > already exists";
    }
}
