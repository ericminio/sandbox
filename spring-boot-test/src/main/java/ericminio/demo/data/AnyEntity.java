package ericminio.demo.data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="ANY_ENTITY")
public class AnyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="ANY_FIELD")
    private String field;

    @Column(name="CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name="UPDATE_DATE")
    private LocalDateTime updateDate;

    public void setField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}
