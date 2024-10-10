package com.globaroman.english.service.impl;

import com.globaroman.english.dto.DataFromDataBaseDto;
import com.globaroman.english.repository.CustomTableService;
import com.globaroman.english.service.LoadWorldFromDataBase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadWorldFromDataBaseImpl implements LoadWorldFromDataBase {

    private final CustomTableService customTableRepository;

    @Override
    public List<DataFromDataBaseDto> getDate() {
        return customTableRepository.getCustomData();
    }

}
