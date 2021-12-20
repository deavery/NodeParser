package com.deavery.dao.impl;

import com.deavery.dao.Md5Dao;
import com.deavery.model.Md5Operation;
import com.deavery.model.Random;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
@AllArgsConstructor
@Slf4j
public class Md5DaoImpl implements Md5Dao {

    private final JdbcTemplate jdbcTemplate;

    private static Md5Operation mapMd5Operation(ResultSet rs, int rowNum) throws SQLException {
        return new Md5Operation(
                rs.getString("operation"),
                rs.getInt("count"),
                rs.getInt("min"),
                rs.getInt("max")
        );
    }

    private static Random mapRandom(ResultSet rs, int rowNum) throws SQLException {
        return new Random(
                rs.getInt("real"),
                rs.getString("hash")
        );
    }

    @Override
    public String createOperation(Md5Operation operation, boolean generate) {
        String sql = "INSERT INTO operation (count, min, max, generate) VALUES (?, ?, ?, ?);";
        KeyHolder genKey = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, operation.getCount());
            statement.setInt(2, operation.getMin());
            statement.setInt(3, operation.getMax());
            statement.setBoolean(4, generate);
            return statement;
        }, genKey);
        return String.valueOf(genKey.getKeys().get("operation"));
    }

    @Override
    public void pushResult(String uuid, List<Random> hashList) {
        log.info("Operation: " + uuid + " " + hashList);
        String sql = "INSERT INTO result (operation_uuid, hash, real) VALUES (?::uuid, ?, ?)";
        jdbcTemplate.update(con -> {
           PreparedStatement statement = con.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
            int i = 0;
            for (Random hash : hashList) {
                statement.setString(1, uuid);
                statement.setString(2, hash.getHash());
                statement.setInt(3, hash.getValue());
                statement.addBatch();
                i++;
                if (i % 1000 == 0 || i == hashList.size()-1) {
                    statement.executeBatch();
                }
            }
           return statement;
        });
    }

    @Override
    public Md5Operation readOperation(String operation) {
        return jdbcTemplate.queryForObject("SELECT operation, count, min, max FROM operation WHERE operation = ?::uuid",  Md5DaoImpl::mapMd5Operation, operation);
    }

    @Override
    public List<Random> readOperationResult(String operation) {
        return jdbcTemplate.query("SELECT hash, real FROM result WHERE operation_uuid = ?::uuid",  Md5DaoImpl::mapRandom, operation);
    }

}
