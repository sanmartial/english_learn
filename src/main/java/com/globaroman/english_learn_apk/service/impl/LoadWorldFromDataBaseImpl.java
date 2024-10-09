package com.globaroman.english_learn_apk.service.impl;

import com.globaroman.english_learn_apk.dto.DataFromDataBaseDto;
import com.globaroman.english_learn_apk.repository.CustomTableService;
import com.globaroman.english_learn_apk.service.LoadWorldFromDataBase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadWorldFromDataBaseImpl implements LoadWorldFromDataBase {

    private final CustomTableService customTableRepository;

    @Override
    public List<DataFromDataBaseDto> getDate() {
        return customTableRepository.getCustomData();
    }

}
