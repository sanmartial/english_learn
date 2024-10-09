package com.globaroman.english_learn_apk.service;

import com.globaroman.english_learn_apk.model.DictionaryWord;

import java.util.List;

public interface DictionaryService {

    public void loadToDataBase(List<DictionaryWord> list);
}
