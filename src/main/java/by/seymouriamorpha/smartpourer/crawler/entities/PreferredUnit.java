package by.seymouriamorpha.smartpourer.crawler.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author seymouria on 03.06.2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreferredUnit {

    @Field
    private String name;
    @Field
    private double value;
    @Field
    private String uom;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }

    public String getUom() {
        return uom;
    }
    public void setUom(String uom) {
        this.uom = uom;
    }

}
