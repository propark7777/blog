package com.park.blog.repository;

import com.park.blog.connection.DBConnectionUtil;
import com.park.blog.domain.plan.Plan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlanRepositoryV0 {

    public Plan savePlan(Plan plan) throws SQLException {
        String sql =
            "insert into plan(exercise_name,exercise_set_number,exercise_repetition) values(?,?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,plan.getExerciseName());
            pstmt.setInt(2,plan.getExerciseSetNumber());
            pstmt.setInt(3,plan.getExerciseRepetition());
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
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("error",e);
            }
        }

        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error("error",e);
            }
        }
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("error",e);
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
