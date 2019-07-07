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
        Group clean = new Group(group.getPersons());
        clean.setOwner(group.getOwner());

        int rightIndex = group.getPersons().indexOf(this.right);
        int leftIndex = group.getPersons().indexOf(this.left);
        int ownerIndex = group.getPersons().indexOf(group.getOwner());
        if (rightIndex >= 0 && leftIndex >= 0) {
            if (leftIndex == ownerIndex) {
                group.getPersons().remove(rightIndex);
            }
            else if (rightIndex == ownerIndex) {
                group.getPersons().remove(leftIndex);
            }
            else {
                if (rightIndex > leftIndex) {
                    group.getPersons().remove(rightIndex);
                }
                else {
                    group.getPersons().remove(leftIndex);
                }
            }
        }

        return clean;
    }
}
