package com.globaroman.english.service.impl;

import com.globaroman.english.service.ProcessingDataService;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.stereotype.Service;

@Service
public class ProcessingDataServiceImpl implements ProcessingDataService {

    @Override
    public Set<String> getUniqueDataAfterProcess(List<String> dataFromFiles) {
        Set<String> setsWords = new TreeSet<>();

        for (String data : dataFromFiles) {
            String[] words = data.split(" ");
            for (String s : words) {
                if (!s.isEmpty()) {
                    setsWords.add(cleanWord(s));
                }
            }
        }

        return setsWords;
    }

    private String cleanWord(String word) {
        return word.replaceAll("[^a-zA-Z]", "").toLowerCase();
    }
}
