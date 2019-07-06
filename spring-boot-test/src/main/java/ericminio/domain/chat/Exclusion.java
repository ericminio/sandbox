package ericminio.domain.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Exclusion implements Serializable {

    private Person left;
    private Person right;

    public Exclusion() {
    }

    public Exclusion(Person left, Person right) {
        this.left = left;
        this.right = right;
    }

    public Person getLeft() {
        return left;
    }

    public void setLeft(Person left) {
        this.left = left;
    }

    public Person getRight() {
        return right;
    }

    public void setRight(Person right) {
        this.right = right;
    }

    public Group visit(Group group) {
        List<Person> persons = group.getPersons();
        Group clean = new Group();
        List<Person> candidates = new ArrayList<>();
        clean.setPersons(candidates);

        candidates.add(persons.get(0));
        candidates.add(persons.get(1));

        return clean;
    }
}
