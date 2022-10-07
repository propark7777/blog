package com.park.blog.repository;

import com.park.blog.connection.DBConnectionUtil;
import com.park.blog.domain.plan.Plan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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

    public List<Plan> findAllPlan() {
        String sql = "select * from plan";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Plan> plans = new ArrayList<>();

            while (rs.next()) {
                Plan plan = new Plan();
                plan.setExerciseName(rs.getString("exercise_name"));
                plan.setExerciseSetNumber(rs.getInt("exercise_set_number"));
                plan.setExerciseRepetition(rs.getInt("exercise_repetition"));
                plans.add(plan);
            }
            return plans;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }

    public Plan findPlanByExerciseId(Long exerciseId) throws SQLException {
        String sql = "select * from plan where exercise_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1,exerciseId);

            rs = pstmt.executeQuery();

            if(rs.next()) {
                Plan plan = new Plan();
                plan.setExerciseId(rs.getLong("exercise_id"));
                plan.setExerciseName(rs.getString("exercise_name"));
                plan.setExerciseSetNumber(rs.getInt("exercise_set_number"));
                plan.setExerciseRepetition(rs.getInt("exercise_repetition"));
                return plan;
            }else {
                throw new NoSuchElementException("exercise not found!!");
            }
        } catch (SQLException e) {
            log.error("db error",e);
            throw e;
        }finally {
            close(conn,pstmt,rs);
        }

    }

    public void updatePlan(Long exerciseId, Plan plan) throws SQLException {
        String sql = "update plan set exercise_name=? , exercise_set_number=? ," +
            "exercise_repetition=? where exercise_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,plan.getExerciseName());
            pstmt.setInt(2,plan.getExerciseSetNumber());
            pstmt.setInt(3,plan.getExerciseRepetition());
            pstmt.setLong(4,exerciseId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}",resultSize);
        } catch (SQLException e) {
            log.error("db error",e);
            throw e;
        }finally {
            close(conn,pstmt,null);
        }

    }

    public void deletePlan(Long exerciseId) throws SQLException {
        String sql = "delete plan where exercise_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1,exerciseId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error",e);
            throw e;
        }finally {
            close(conn,pstmt,null);
        }

    }

    private void close(Connection conn, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(conn,dataSource);
//        JdbcUtils.closeConnection(conn);
    }

    private Connection getConnection() throws SQLException {
        //Connection conn = dataSource.getConnection();
        Connection conn = DataSourceUtils.getConnection(dataSource);
        log.info("get Connection = {} class = {}",conn,conn.getClass());
        return conn;
    }
}
