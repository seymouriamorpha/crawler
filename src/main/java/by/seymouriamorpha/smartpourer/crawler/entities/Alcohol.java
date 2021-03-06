package by.seymouriamorpha.smartpourer.crawler.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;

/**
 * @author Eugene_Kortelyov on 5/31/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Alcohol {

    @Id
    private String id;
    @Field
    private String name;
    @Field
    private ArrayList<?> categories;
    @Field
    private int[] preferredUnit;

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

    public ArrayList<?> getCategories() {
        return categories;
    }
    public void setCategories(ArrayList<?> categories) {
        this.categories = categories;
    }

    public int[] getPreferredUnit() {
        return preferredUnit;
    }
    public void setPreferredUnit(int[] preferredUnit) {
        this.preferredUnit = preferredUnit;
    }

}
