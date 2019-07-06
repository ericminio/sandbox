package ericminio.domain.chat;

import java.io.Serializable;

public class Person implements Serializable {

    private String name;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Person)) {
            return false;
        }
        Person other = (Person) o;

        return other.name.equalsIgnoreCase(this.name);
    }
}
