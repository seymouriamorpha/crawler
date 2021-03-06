package by.seymouriamorpha.smartpourer.crawler.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Eugene_Kortelyov on 5/31/2017.
 */
@Document(collection = "spirit")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Spirit {

    @Id
    private String id;
    @Field
    private String name;
    @Field
    private ArrayList<Spirit> categories;
    @Field
    private ArrayList<PreferredUnit> preferredUnit;
    @Field
    private Double volume;
    @Field
    private Double value;
    @Field
    private String productURL;
    @Field
    private String imageURL;

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

    public ArrayList<Spirit> getCategories() {
        return categories;
    }
    public void setCategories(ArrayList<Spirit> categories) {
        this.categories = categories;
    }

    public ArrayList<PreferredUnit> getPreferredUnit() {
        return preferredUnit;
    }
    public void setPreferredUnit(ArrayList<PreferredUnit> preferredUnit) {
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

    public String getProductURL() {
        return productURL;
    }
    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Spirit other = (Spirit) obj;
        return Objects.equals(name, other.name) && value == other.value && volume == other.volume;
    }

}
