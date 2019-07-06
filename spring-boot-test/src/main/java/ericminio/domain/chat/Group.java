package ericminio.domain.chat;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {

    private List<Person> persons;

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public String toString() {
        return persons.toString();
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
}
