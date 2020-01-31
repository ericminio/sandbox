package ericminio.demo.data;

import javax.persistence.*;

@Entity
@Table(name="ANY_ENTITY")
public class AnyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="ANY_FIELD")
    private String field;

    public void setField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
