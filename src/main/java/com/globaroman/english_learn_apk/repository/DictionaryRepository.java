package com.globaroman.english_learn_apk.repository;

import com.globaroman.english_learn_apk.model.DictionaryWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictionaryRepository extends JpaRepository<DictionaryWord, Long> {
}
