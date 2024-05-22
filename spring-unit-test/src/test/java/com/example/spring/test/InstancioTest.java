package com.example.spring.test;

import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.TypeToken;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.instancio.Select.all;
import static org.instancio.Select.field;

@ExtendWith(value = {MockitoExtension.class, InstancioExtension.class})
public class InstancioTest {

    @Test
    public void createInstance() {
        // Create by specifying the class
        Person person = Instancio.create(Person.class);

// Using type tokens
        Pair<String, Long> pair = Instancio.create(new TypeToken<Pair<String, Long>>() {});

        Map<Integer, List<String>> map = Instancio.create(new TypeToken<Map<Integer, List<String>>>() {});

// Create from a model
        Model<Person> personModel = Instancio.of(Person.class)
                .ignore(field(Person::getName))
                .toModel();

        Person personWithoutAgeAndAddress = Instancio.of(personModel)
                .ignore(field(Person::getHomeAddress))
                .create();

        List<Person> list = Instancio.createList(Person.class);

        List<Person> list1 = Instancio.ofList(Person.class).size(10).create();

        List<Pair<String, Integer>> list2 = Instancio.ofList(new TypeToken<Pair<String, Integer>>() {}).create();

        Map<UUID, Address> map2 = Instancio.ofMap(UUID.class, Address.class).size(3)
                .set(field(Address::getCity), "Vancouver")
                .create();

        // Create from a model
        Model<Person> personModel2 = Instancio.of(Person.class)
                .ignore(field(Person::getName)).toModel();

        Set<Person> set = Instancio.ofSet(personModel2).size(5).create();

        List<Person> personList = Instancio.stream(Person.class)
                .limit(3)
                .collect(Collectors.toList());

        Map<String, Person> personMap = Instancio.of(new TypeToken<Person>() {})
                .ignore(all(field(Person::getWorkAddress), field(Person::getHomeAddress)))
                .stream()
                .limit(3)
                .collect(Collectors.toMap(Person::getName, Function.identity()));
    }
}


class Person {
    String name;
    Address homeAddress;
    Address workAddress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(Address workAddress) {
        this.workAddress = workAddress;
    }
}

class Address {
    String street;
    String city;
    List<Phone> phoneNumbers;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Phone> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<Phone> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}

class Phone {
    String areaCode;
    String number;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
