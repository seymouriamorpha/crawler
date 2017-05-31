package by.seymouriamorpha.smartpourer.crawler.entities;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author Eugene_Kortelyov on 5/31/2017.
 */
@Document(collection = "spirits")
public class Spirit extends Alcohol {

    /* % */
    @Field
    private double volume;
    /* amount */
    @Field
    private double value;
    @Field
    private String productURL;
    @Field
    private String imageURL;

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

}
