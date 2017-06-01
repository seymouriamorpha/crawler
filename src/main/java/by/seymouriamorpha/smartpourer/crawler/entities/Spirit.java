package by.seymouriamorpha.smartpourer.crawler.entities;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author Eugene_Kortelyov on 5/31/2017.
 */
@Document(collection = "spirit")
public class Spirit extends Alcohol {

    /* % */
    @Field
    private Double volume;
    /* amount */
    @Field
    private Double value;
    @Field
    private String productURL;
    @Field
    private String imageURL;

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

}
