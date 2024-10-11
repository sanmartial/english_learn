package com.globaroman.english.service;

import java.util.List;
import java.util.Set;

public interface ProcessingDataService {
    Set<String> getUniqueDataAfterProcess(List<String> dataFromFiles);
}
