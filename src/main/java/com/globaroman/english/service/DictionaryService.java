package com.globaroman.english.service;

import com.globaroman.english.model.DictionaryWord;
import java.util.List;

public interface DictionaryService {

    void loadToDataBase(List<DictionaryWord> list);

    String loadToDataBaseIfNoThisWord(List<String> list);

    String getRandomWordFromDB();

    String saveNewWord(String line);
}
