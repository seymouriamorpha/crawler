package by.seymouriamorpha.smartpourer.crawler;

import by.seymouriamorpha.smartpourer.crawler.entities.Spirit;
import by.seymouriamorpha.smartpourer.crawler.entities.TempSpirit;
import by.seymouriamorpha.smartpourer.crawler.repository.SpringMongoConfig;
import by.seymouriamorpha.smartpourer.crawler.utils.IOUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;

/**
 * @author Eugene_Kortelyov on 5/31/2017.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        ArrayList<Spirit> firstLvlSpirits = IOUtil.readSpiritsFromFile("D:\\alcohol_data\\1lvl.json");

        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        mongoOperation.insert(firstLvlSpirits, Spirit.class);
        System.out.println("1st layer complete");

        loadBaseLayers("D:\\alcohol_data\\2lvl.json", mongoOperation);
        System.out.println("2nd layer complete");
        loadBaseLayers("D:\\alcohol_data\\3lvl.json", mongoOperation);
        System.out.println("3rd layer complete");
    }

    private static void loadBaseLayers(String filepath,  MongoOperations mongoOperation){
        ArrayList<TempSpirit> layer = IOUtil.readTempSpiritsFromFile(filepath);
        for (TempSpirit tempSpirit: layer){
            Spirit spirit = new Spirit();
            spirit.setId(tempSpirit.getId());
            spirit.setName(tempSpirit.getName());
            spirit.setValue(tempSpirit.getValue());
            spirit.setVolume(tempSpirit.getVolume());
            ArrayList<Spirit> categories = new ArrayList<>();
            Spirit base = mongoOperation.findOne(new Query(Criteria.where("_id").is(tempSpirit.getCategory())), Spirit.class);
            categories.add(base);
            spirit.setCategories(categories);
            mongoOperation.insert(spirit);
        }
    }
}
