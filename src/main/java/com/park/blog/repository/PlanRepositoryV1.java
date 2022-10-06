package com.park.blog.repository;

import com.park.blog.connection.DBConnectionUtil;
import com.park.blog.domain.plan.Plan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PlanRepositoryV1 {
    private final DataSource dataSource;

    public Plan savePlan(Plan plan) throws SQLException {
        String sql = "insert into plan(exercise_name,exercise_set_number,exercise_repetition) values(?,?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            log.info("logInfo ****************");
            pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,plan.getExerciseName());
            pstmt.setInt(2,plan.getExerciseSetNumber());
            pstmt.setInt(3,plan.getExerciseRepetition());
            log.info("세트 갯수: {}",plan.getExerciseSetNumber());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                plan.setExerciseId(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }

            return plan;

        } catch (SQLException e) {
            throw e;
        }finally {
            close(conn,pstmt,null);
        }
    }

    private void close(Connection conn, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(conn);
    }

    private Connection getConnection() throws SQLException {
        //Connection conn = dataSource.getConnection();
        Connection conn = DataSourceUtils.getConnection(dataSource);
        log.info("get Connection = {} class = {}",conn,conn.getClass());
        return conn;
    }
}
