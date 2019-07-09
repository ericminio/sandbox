package ericminio.demo.chat.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Group implements Serializable {

    private ArrayList<Person> persons;
    private Person owner;

    public Group() {

    }

    public Group(Person ... persons) {
        this.persons = new ArrayList<>(Arrays.asList(persons));
    }

    public Group(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Group)) {
            return false;
        }
        Group other = (Group) o;
        if (other.persons.size() != this.persons.size()) {
            return false;
        }
        for (int i=0; i<this.persons.size(); i++) {
            if (!(this.persons.get(i).equals(other.persons.get(i)))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return persons.toString();
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Person getOwner() {
        return owner;
    }
}
