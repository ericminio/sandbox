package ericminio;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class DefaultMethodTest {

    @Test
    public void isAvailableToExtendingClasses() {
        class Dog implements Animal {}

        assertThat( new Dog().isAlive(), equalTo( true ) );
    }

    @Test
    public void canBeOverridden() {
        class DeadDog implements Animal {
            @Override
            public boolean isAlive() {
                return false;
            }
        }

        assertThat( new DeadDog().isAlive(), equalTo( false ) );
    }

    interface Animal {
        default boolean isAlive() {
            return true;
        }
    }
}
