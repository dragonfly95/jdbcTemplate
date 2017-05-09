package com.logicbig.example;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class AppController {
    private static final Logger logger = Logger.getLogger(AppController.class.getName());

    @Autowired
    PersonService personService;

    public void process() {


        List<Person> persons = Arrays.asList(Person.create("Dana", "Whitley", "464 Gorsuch Drive"),
                Person.create("Robin", "Cash", "64 Zella Park"),
                Person.create("Terry", "Roberson", "168 Lakewood Square")
        );

        //this will be executed in a single session
        personService.bulkSave(persons);

        //this will open  and close connections three times.
      /*  for (Person person : persons) {
            personService.savePerson(person);
        }*/
        logger.info("All persons " + personService.getAllPersons());
    }
}
