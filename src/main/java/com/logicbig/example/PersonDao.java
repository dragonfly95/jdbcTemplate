package com.logicbig.example;

import java.util.List;

public interface PersonDao extends Dao<Person> {

    List<Person> findPersonsByLastName(String firstName);

    Long getPersonCount();

    void updateAddress(long id, String newAddress);

    int[][] bulkSave(List<Person> newPersons);
}
