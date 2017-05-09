package com.logicbig.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class JdbcTemplatePersonDao implements PersonDao {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int[][] bulkSave(List<Person> newPersons) {

        final List<Person> persons = Arrays.asList(
                Person.create("Dana", "Whitley", "464 Gorsuch Drive"),
                Person.create("Robin", "Cash", "64 Zella Park")
        );

        String sql = "insert into Person (first_Name, Last_Name, Address) values (?, ?, ?)";

        return jdbcTemplate.batchUpdate(sql, persons, persons.size(),

                new ParameterizedPreparedStatementSetter<Person>() {
                    @Override
                    public void setValues(PreparedStatement ps, Person person) throws SQLException {
                        ps.setString(1, person.getFirstName());
                        ps.setString(2, person.getLastName());
                        ps.setString(3, person.getAddress());
                    }
                });
    }

    @Override
    public void save(Person person) {
        String sql = "insert into Person (first_Name, Last_Name, Address) values (?, ?, ?)";
        jdbcTemplate.update(sql, person.getFirstName(), person.getLastName(),
                person.getAddress());
    }

    @Override
    public Person load(long id) {
        List<Person> persons = jdbcTemplate.query("select * from Person where id =?",
                new Object[]{id}, (resultSet, i) -> {
                    return toPerson(resultSet);
                });

        if (persons.size() == 1) {
            return persons.get(0);
        }
        return null;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from PERSON where id = ?", id);
    }

    @Override
    public void update(Person person) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateAddress(long id, String newAddress) {
        jdbcTemplate.update("update PERSON set ADDRESS = ? where ID = ?"
                , newAddress, id);
    }

    @Override
    public List<Person> loadAll() {
        return jdbcTemplate.query("select * from Person", (resultSet, i) -> {
            return toPerson(resultSet);
        });
    }

    private Person toPerson(ResultSet resultSet) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getLong("ID"));
        person.setFirstName(resultSet.getString("FIRST_NAME"));
        person.setLastName(resultSet.getString("LAST_NAME"));
        person.setAddress(resultSet.getString("ADDRESS"));
        return person;
    }


    @Override
    public List<Person> findPersonsByLastName(String name) {
        return jdbcTemplate.query("select * from Person where LAST_NAME = ?",
                new Object[]{name}, new RowMapper<Person>() {
                    @Override
                    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
                        return toPerson(resultSet);
                    }
                });
    }

    @Override
    public Long getPersonCount() {
        return jdbcTemplate.queryForObject("select count(*) from PERSON",
                Long.class);

    }
}
