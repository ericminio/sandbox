package ericminio.concurrency;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ConcurrentHashMapTest {

    ConcurrentHashMap<Object, String> dictionary;

    @Before
    public void sut() {
        dictionary = new ConcurrentHashMap<>();
    }

    @Test
    public void doesNotMatchObjectsByDefault() {
        dictionary.put(new Product("keyboard"), "match");

        assertThat(dictionary.containsKey(new Product("KEYBOARD")), equalTo(false));
    }
    @Test
    public void equalsIsNotEnough() {
        dictionary.put(new ProductWithCorrectEquals("keyboard"), "match");

        assertThat(dictionary.containsKey(new ProductWithCorrectEquals("KEYBOARD")), equalTo(false));
    }
    @Test
    public void hasCodeIsNotEnough() {
        dictionary.put(new ProductWithCorrectHashCode("keyboard"), "match");

        assertThat(dictionary.containsKey(new ProductWithCorrectHashCode("KEYBOARD")), equalTo(false));
    }
    @Test
    public void bothAreNeeded() {
        dictionary.put(new ProductWithBothCorrectEqualsAndHashCode("keyboard"), "match");

        assertThat(dictionary.containsKey(new ProductWithBothCorrectEqualsAndHashCode("KEYBOARD")), equalTo(true));
    }
    class Product {
        protected String name;

        public Product(String name) {
            this.name = name;
        }
        @Override
        public boolean equals(Object other) {
            return this.name.equals(((Product) other).name);
        }
        @Override
        public int hashCode() {
            return this.name.hashCode();
        }
    }
    class ProductWithCorrectEquals extends Product {

        public ProductWithCorrectEquals(String name) {
            super(name);
        }
        @Override
        public boolean equals(Object other) {
            return this.name.equalsIgnoreCase(((Product) other).name);
        }
    }
    class ProductWithCorrectHashCode extends Product {

        public ProductWithCorrectHashCode(String name) {
            super(name);
        }
        @Override
        public int hashCode() {
            return this.name.toLowerCase().hashCode();
        }
    }
    class ProductWithBothCorrectEqualsAndHashCode extends Product {

        public ProductWithBothCorrectEqualsAndHashCode(String name) {
            super(name);
        }
        @Override
        public boolean equals(Object other) {
            return this.name.equalsIgnoreCase(((Product) other).name);
        }
        @Override
        public int hashCode() {
            return this.name.toLowerCase().hashCode();
        }
    }
}
