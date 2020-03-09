package ericminio.demo.data;

import javax.persistence.*;

@SqlResultSetMapping(
        name = "ReportMapping",
        entities = {
                @EntityResult(
                        entityClass = Report.class,
                        fields = {
                                @FieldResult( name = "value", column = "value" ),
                                @FieldResult( name = "count", column = "value_count" )
                        }
                )
        }
)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GroupByValue",
                query = "SELECT ANY_FIELD as value, count(1) as value_count FROM ANY_ENTITY GROUP BY ANY_FIELD",
                resultSetMapping = "ReportMapping"
        )
})

@Entity
public class Report {

    @Id
    private String value;
    private Integer count;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
