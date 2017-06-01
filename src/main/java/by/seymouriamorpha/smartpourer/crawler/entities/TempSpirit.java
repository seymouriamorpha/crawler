package by.seymouriamorpha.smartpourer.crawler.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author Eugene_Kortelyov on 5/31/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TempSpirit {

    @Id
    private String id;
    @Field
    private String name;
    @Field
    private String[] categories;
    @Field
    private int[] preferredUnit;
    @Field
    private Double volume;
    @Field
    private Double value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public int[] getPreferredUnit() {
        return preferredUnit;
    }

    public void setPreferredUnit(int[] preferredUnit) {
        this.preferredUnit = preferredUnit;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

}
