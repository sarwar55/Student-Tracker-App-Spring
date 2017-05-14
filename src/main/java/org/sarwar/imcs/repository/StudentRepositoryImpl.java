package org.sarwar.imcs.repository;

import org.sarwar.imcs.model.Student;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {

    private static final String SQL = "select * from student order by last_name";
    private static final String ADD_SQL = "insert into student " + "(first_name, last_name, email) "
            + "values (?, ?, ?)";

    private static final String UPDATE_SQL = "update student " + "set first_name=?, last_name=?, email=? "
            + "where id=?";
    private static final String DELETE_SQL = "delete from student where id=?";
    private static final String SEARCH_SQL = "select * from student where lower(first_name) like ? or lower(last_name) like ?";
    private JdbcTemplate jdbcTemplate;

    public StudentRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Student> getStudents() {
        List<Student> students = jdbcTemplate.query(SQL, new RowMapper<Student>() {
            @Override
            public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                return getStudent(resultSet, rowNum);
            }
        });
        return students;
    }

    public void addStudent(Student student) {
        jdbcTemplate.update(ADD_SQL, student.getFirstName(), student.getLastName(),
                student.getEmail());
    }

    public Student getStudent(String studentId) {
        final String GET_SQL = "select * from student where id=" + studentId;
        return jdbcTemplate.query(GET_SQL, new ResultSetExtractor<Student>() {
            @Override
            public Student extractData(ResultSet resultSet) throws SQLException,
                    DataAccessException {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    Student student = new Student(Integer.parseInt(studentId), firstName, lastName, email);
                    return student;
                }
                return null;
            }
        });
    }

    public void updateStudent(Student student) {
        jdbcTemplate.update(UPDATE_SQL, student.getFirstName(), student.getLastName(),
                student.getEmail(), student.getId());
    }

    public void deleteStudent(String studentId) {
        jdbcTemplate.update(DELETE_SQL, studentId);
    }

    public List<Student> searchStudents(String searchName) {
        List<Student> students = new ArrayList<>();
        if (searchName != null && searchName.trim().length() > 0) {
            students = jdbcTemplate.query(SEARCH_SQL, new PreparedStatementSetter() {
                public void setValues(PreparedStatement preparedStatement) throws
                        SQLException {
                    String searchNameLike = "%" + searchName.toLowerCase() + "%";
                    preparedStatement.setString(1, searchNameLike);
                    preparedStatement.setString(2, searchNameLike);
                }
            }, new RowMapper<Student>() {
                @Override
                public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                    return getStudent(resultSet, rowNum);
                }
            });
        } else {
            students = jdbcTemplate.query(SQL, new RowMapper<Student>() {
                @Override
                public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                    return getStudent(resultSet, rowNum);
                }
            });
        }
        return students;
    }

    private Student getStudent(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        return new Student(id, firstName, lastName, email);
    }
}