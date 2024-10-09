package com.globaroman.english_learn_apk.service.impl;

import com.globaroman.english_learn_apk.model.DictionaryWord;
import com.globaroman.english_learn_apk.repository.DictionaryRepository;
import com.globaroman.english_learn_apk.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {
    private final DictionaryRepository dictionaryRepository;

    @Override
    public void loadToDataBase(List<DictionaryWord> list) {
        dictionaryRepository.saveAll(list);
    }
}
