package com.globaroman.english_learn_apk.controller;

import com.globaroman.english_learn_apk.service.LoadWorldFromDataBase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataController {
    private final LoadWorldFromDataBase loadWorldFromDataBase;

    public void checkData() {
    }
}
