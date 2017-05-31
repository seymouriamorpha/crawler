package by.seymouriamorpha.smartpourer.crawler.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;

/**
 * @author Eugene_Kortelyov on 5/31/2017.
 */
public class TempSpirit {

    @Id
    private String id;
    @Field
    private String name;
    @Field
    private String category;
    @Field
    private int[] preferredUnit;
    @Field
    private double volume;
    @Field
    private double value;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int[] getPreferredUnit() {
        return preferredUnit;
    }

    public void setPreferredUnit(int[] preferredUnit) {
        this.preferredUnit = preferredUnit;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
