package com.globaroman.english.service.impl;

import com.globaroman.english.model.DictionaryWord;
import com.globaroman.english.repository.DictionaryRepository;
import com.globaroman.english.service.AwsTranslateService;
import com.globaroman.english.service.DictionaryService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final AwsTranslateService translateService;

    @Override
    public String getRandomWordFromDB() {
        DictionaryWord word = dictionaryRepository.findRandomWord();
        word.setWordLearned(true);
        dictionaryRepository.save(word);
        return word.getEnglishWord() + " : " + word.getTranslatedWord();
    }

    @Override
    public String saveNewWord(String line) {

        if (dictionaryRepository.existsByEnglishWord(line)) {
            return line + " вже існує!";
        } else {
            DictionaryWord word = getNewWord(line);
            return "Добавлено: " + word.getEnglishWord() + " : " + word.getTranslatedWord();
        }
    }

    @Override
    public String loadNewWordToDataBase(Set<String> afterProcessDatas) {
        StringBuilder sb = new StringBuilder();
        sb.append("Добавлено: \n");
        for (String data : afterProcessDatas) {
            if (!dictionaryRepository.existsByEnglishWord(data) && !data.isEmpty()) {
                DictionaryWord word = getNewWord(data);
                sb.append(word.getEnglishWord()).append(" : ")
                        .append(word.getTranslatedWord()).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String countNewWords() {
        int countNewWord = dictionaryRepository.countByLearnedWordFalse();
        return "База даних включає " + countNewWord + " нових слів";
    }

    private DictionaryWord getNewWord(String line) {
        DictionaryWord word = new DictionaryWord();
        word.setEnglishWord(line);
        word.setTranslatedWord(translateService.translateText(line,
                "en", "ru"));
        word.setWordLearned(false);
        dictionaryRepository.save(word);
        return word;
    }
}
