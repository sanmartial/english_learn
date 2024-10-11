package com.globaroman.english.service.impl;

import com.globaroman.english.service.ReadWriteFromToFileService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReadWriteFromToFileServiceImpl implements ReadWriteFromToFileService {

    @Override
    public List<String> readDataFromFile(String fileUrl) {
        List<String> list = new ArrayList<>();
        try {
            URL url = new URL(fileUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;

            while ((line = in.readLine()) != null) {
                if (!line.isEmpty() && !line.isBlank()) {
                    list.add(line);
                }
            }
            in.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
