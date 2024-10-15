package com.globaroman.english.repository;

import com.globaroman.english.model.DictionaryWord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DictionaryRepository extends JpaRepository<DictionaryWord, Long> {

    @Query("select d from DictionaryWord d where d.englishWord = :word")
    List<DictionaryWord> findByWord(@Param("word") String englishWord);

    @Query(value = "SELECT * FROM dictionary_words where learned_word = "
            + "false ORDER BY RAND() LIMIT 1",
            nativeQuery = true)
    DictionaryWord findRandomWord();

    @Query(value = "SELECT count(*) FROM dictionary_words where learned_word = false",
            nativeQuery = true)
    int countByLearnedWordFalse();

    boolean existsByEnglishWord(String line);

    @Query(value = "SELECT * FROM dictionary_words where learned_word = "
            + "false ORDER BY RAND() LIMIT :countWords",
            nativeQuery = true)
    List<DictionaryWord> findRandEnglishWord(@Param("countWords")int countWords);
}
