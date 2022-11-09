package com.park.blog.repository;

import com.park.blog.domain.dailycheck.DailyCheck;
import com.park.blog.domain.dailycheck.DailyCheckForm;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
@Slf4j
@Repository
public class DailyCheckRepository {
    private final JdbcTemplate template;
    private final Map<Long,DailyCheck> localStore = new HashMap<>();

    public DailyCheckRepository(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public DailyCheck saveInLocal(DailyCheck dailyCheck) {
        localStore.put(dailyCheck.getDailyCheckId(), dailyCheck);
        return dailyCheck;
    }

    public DailyCheck findSaveInLocalById(Long id) {
        return localStore.get(id);
    }

    public DailyCheck saveDailyCheck(DailyCheck dailyCheck) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into DailyCheck(dailyCheckName) values(?)";
        template.update(
            con -> {
                PreparedStatement pstmt = con.prepareStatement(sql,new String[] {"dailyCheckId"});
                pstmt.setString(1,dailyCheck.getDailyCheckName());
                return pstmt;
            },keyHolder);
        Long key = keyHolder.getKey().longValue();
        log.info("key = {}",key);
        dailyCheck.setDailyCheckId(key);
        return dailyCheck;
    }

    public DailyCheck findById(Long dailyCheckId) {
        String sql = "select * from dailyCheck where dailyCheckId = ?";
        return template.queryForObject(sql,dailyCheckRowMapper(),dailyCheckId);
    }

    private RowMapper<DailyCheck> dailyCheckRowMapper() {
        return (rs, rowNum) -> {
            DailyCheck dailyCheck = new DailyCheck();
            dailyCheck.setDailyCheckId(rs.getLong("dailyCheckId"));
            dailyCheck.setDailyCheckName(rs.getString("dailyCheckName"));
            log.info("dailyCheckId = {}",dailyCheck.getDailyCheckId());
            log.info("dailyCheckName = {}",dailyCheck.getDailyCheckName());
            return dailyCheck;
        };
    }
}
