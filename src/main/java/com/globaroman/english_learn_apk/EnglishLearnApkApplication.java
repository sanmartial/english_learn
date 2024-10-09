package com.globaroman.english_learn_apk;

import com.globaroman.english_learn_apk.config.BotConfig;
import com.globaroman.english_learn_apk.service.LoadWorldFromDataBase;
import com.globaroman.english_learn_apk.service.impl.LoadWorldFromDataBaseImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnglishLearnApkApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnglishLearnApkApplication.class, args);


    }

}
