package ericminio.demo.chat;

import ericminio.domain.chat.Exclusion;
import ericminio.domain.chat.Group;
import ericminio.domain.chat.Person;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ExclusionTest {

    private Group group;

    @Before
    public void init() {
        group = new Group(diana(), joe(), jenny());
        group.setOwner(diana());
    }
    private Person diana() { return new Person("Diana"); }
    private Person joe() { return new Person("Joe"); }
    private Person jenny() { return new Person("Jenny"); }
    private Person clark() { return new Person("Clark"); }
    private Person bruce() { return new Person("Bruce"); }

    @Test
    public void keepsLeftOwner() {
        Exclusion exclusion = new Exclusion(diana(), jenny());
        Group actual = exclusion.visit(group);
        Group expected = new Group(diana(), joe());

        assertThat( actual, equalTo( expected ) );
    }

    @Test
    public void keepsRightOwner() {
        Exclusion exclusion = new Exclusion(jenny(), diana());
        Group actual = exclusion.visit(group);
        Group expected = new Group(diana(), joe());

        assertThat( actual, equalTo( expected ) );
    }

    @Test
    public void ignoresExclusionWithSomeoneNotInTheGroup() {
        Exclusion exclusion = new Exclusion(diana(), clark());
        Group actual = exclusion.visit(group);
        Group expected = new Group(diana(), joe(), jenny());

        assertThat( actual, equalTo( expected ) );
    }

    @Test
    public void ignoresUnrelatedExclusion() {
        Exclusion exclusion = new Exclusion(clark(), bruce());
        Group actual = exclusion.visit(group);
        Group expected = new Group(diana(), joe(), jenny());

        assertThat( actual, equalTo( expected ) );
    }

    @Test
    public void removesFirstAddedInGroupWhenLeftExcluded() {
        Exclusion exclusion = new Exclusion(joe(), jenny());
        Group actual = exclusion.visit(group);
        Group expected = new Group(diana(), joe());

        assertThat( actual, equalTo( expected ) );
    }

    @Test
    public void removesFirstAddedInGroupWhenRightExcluded() {
        Exclusion exclusion = new Exclusion(jenny(), joe());
        Group actual = exclusion.visit(group);
        Group expected = new Group(diana(), joe());

        assertThat( actual, equalTo( expected ) );
    }
}
