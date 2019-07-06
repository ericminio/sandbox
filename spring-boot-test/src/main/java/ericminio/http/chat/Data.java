package ericminio.http.chat;

import ericminio.domain.chat.Exclusion;
import ericminio.domain.chat.Group;

public class Data {

    private Group group;
    private Exclusion exclusion;

    public Data() {
    }

    public Data(Group group, Exclusion exclusion) {
        this.group = group;
        this.exclusion = exclusion;
    }

    public Exclusion getExclusion() {
        return exclusion;
    }

    public void setExclusion(Exclusion exclusion) {
        this.exclusion = exclusion;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
