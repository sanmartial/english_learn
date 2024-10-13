package com.globaroman.english.service;

import java.util.Set;

public interface DictionaryService {

    String getRandomWordFromDB();

    String saveNewWord(String line);

    String loadNewWordToDataBase(Set<String> afterProcessDatas);

    String countNewWords();
}
