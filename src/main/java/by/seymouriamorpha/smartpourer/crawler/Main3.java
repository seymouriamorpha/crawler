package by.seymouriamorpha.smartpourer.crawler;

import by.seymouriamorpha.smartpourer.crawler.entities.Spirit;
import by.seymouriamorpha.smartpourer.crawler.entities.SpiritWithBars;
import by.seymouriamorpha.smartpourer.crawler.repository.SpringMongoConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * @author Eugene_Kortelyov on 6/12/2017.
 */
public class Main3 {

    public static void main(String[] args) throws Exception {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

        ArrayList<String> links = new ArrayList<>();
        links.add("https://www.proof66.com/liquor/whiskey.html");
        links.add("https://www.proof66.com/liquor/vodka.html");
        links.add("https://www.proof66.com/liquor/brandy.html");
        links.add("https://www.proof66.com/liquor/gin.html");
        links.add("https://www.proof66.com/liquor/rum.html");
        links.add("https://www.proof66.com/liquor/tequila.html");

//        ArrayList<SpiritWithBars> spirits = new ArrayList<>();
        ArrayList<SpiritWithBars> result = new ArrayList<>();

        /* get all direct link for category */
        for (String link: links){
            ArrayList<SpiritWithBars> spirits = new ArrayList<>();
            System.out.println("Current category: " + link);

            BrowserEngine browser = BrowserFactory.getWebKit();
            Page page = browser.navigate(link);

//            Document doc = Jsoup.connect(link).get();
            Document doc = Jsoup.parse(page.getDocument().getBody().toString());
            Elements directLinks = doc
                    .select("span.font14s480")
                    .select("span.font600")
                    .select("a");
            for (Element element: directLinks) {
                SpiritWithBars spirit = new SpiritWithBars();
                spirit.setName(element.text());
                spirit.setProductURL(element.attr("href").replace("..", "https://proof66.com"));
                spirits.add(spirit);
            }
            System.out.println("Found " + directLinks.size() + " links");

            ArrayList<Spirit> categories = new ArrayList<>();
            Spirit temp = mongoOperation.findOne(new Query(Criteria.where("_id").is(getCategory(link))), Spirit.class);
            for (Spirit category: temp.getCategories()){
                Spirit cata = mongoOperation.findOne(new Query(Criteria.where("_id").is(category.getId())), Spirit.class);
                categories.add(cata);
            }
            categories.add(temp);

            /* if we found barcodes - get all other data */
            label: for (SpiritWithBars spirit: spirits) {
                ArrayList<String> bars = new ArrayList<>();
                ArrayList<String> bars_res = new ArrayList<>();

                Document document;
                try {
                    document = Jsoup.connect(spirit.getProductURL()).timeout(5000).get();
                } catch (Exception exception){
                    continue label;
                }
                System.out.println("Current alcohol URL: " + spirit.getProductURL());
                Elements barcodes = document.select("li.list-circle");

                for (Element element: barcodes) {
                    bars.add(element.text());
                }

                for (String bar: bars){
                    if (bar.matches("-?\\d+(\\.\\d+)?")){
                        bars_res.add(bar);
                    }
                }

                if (!bars_res.isEmpty()){
                    SpiritWithBars swb = new SpiritWithBars();
                    swb.setBars(bars_res);
                    swb.setName(spirit.getName());
                    swb.setProductURL(spirit.getProductURL());

                    Elements volumes = document.select("span.font16").select("span.fontRatingInfos320");
                    for (Element vol: volumes){
                        if (vol.text().contains("%") && vol.text().length() < 7){
                            swb.setValue(Double.parseDouble(vol.text().replace("%", "")));
                        }
                    }

                    Element image = document.select("ul.mainimage").select("img").first();
                    swb.setImageURL(image.attr("src").replace("..", "https://www.proof66.com/image"));

                    swb.setCategories(categories);

                    int start = swb.getProductURL().lastIndexOf("/");
                    int end = swb.getProductURL().lastIndexOf(".");
                    swb.setId(swb.getProductURL().substring(start + 1, end));

                    result.add(swb);
                }
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("d:\\proof66.json"), result);

    }

    private static String getCategory(String url){
        switch (url){
            case "https://www.proof66.com/liquor/whiskey.html":
                return "whisky-whiskey";
            case "https://www.proof66.com/liquor/vodka.html":
                return "vodka";
            case "https://www.proof66.com/liquor/brandy.html":
                return "brandy";
            case "https://www.proof66.com/liquor/gin.html":
                return "gin";
            case "https://www.proof66.com/liquor/rum.html":
                return "rum";
            case "https://www.proof66.com/liquor/tequila.html":
                return "tequila";
        }
        return null;
    }

}
