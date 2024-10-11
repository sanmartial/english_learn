package com.globaroman.english.service;

import com.globaroman.english.model.DictionaryWord;
import java.util.List;
import java.util.Set;

public interface DictionaryService {

    void loadToDataBase(List<DictionaryWord> list);

    String loadToDataBaseIfNoThisWord(List<String> list);

    String getRandomWordFromDB();

    String saveNewWord(String line);

    String loadNewWordToDataBase(Set<String> afterProcessDatas);

    String countNewWords();
}
