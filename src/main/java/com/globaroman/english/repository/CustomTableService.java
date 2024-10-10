package com.globaroman.english.repository;

import com.globaroman.english.dto.DataFromDataBaseDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomTableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DataFromDataBaseDto> getCustomData() {
        String sql = "SELECT * FROM noteenglish";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DataFromDataBaseDto dto = new DataFromDataBaseDto();
            dto.setId(rs.getLong("id"));
            dto.setWord(rs.getString("word"));
            return dto;
        });
    }
}

