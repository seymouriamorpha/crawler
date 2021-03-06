package by.seymouriamorpha.smartpourer.crawler;

import by.seymouriamorpha.smartpourer.crawler.entities.Link;
import by.seymouriamorpha.smartpourer.crawler.entities.Spirit;
import by.seymouriamorpha.smartpourer.crawler.entities.TempSpirit;
import by.seymouriamorpha.smartpourer.crawler.repository.SpringMongoConfig;
import by.seymouriamorpha.smartpourer.crawler.utils.IOUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Eugene_Kortelyov on 5/31/2017.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

        /* load base spirits */
        /*ArrayList<Spirit> firstLvlSpirits = IOUtil.readSpiritsFromFile("D:\\workspace\\crawler\\src\\main\\resources\\1lvl.json");
        mongoOperation.insert(firstLvlSpirits, Spirit.class);
        System.out.println("1st layer complete");
        loadBaseLayers("D:\\workspace\\crawler\\src\\main\\resources\\2lvl.json", mongoOperation);
        System.out.println("2nd layer complete");
        loadBaseLayers("D:\\workspace\\crawler\\src\\main\\resources\\3lvl.json", mongoOperation);
        System.out.println("3rd layer complete");
        loadBaseLayers("D:\\workspace\\crawler\\src\\main\\resources\\4lvl.json", mongoOperation);
        System.out.println("4th layer complete");*/

        /* load lcbo data */
        /*ArrayList<Link> lcbolinks = IOUtil.readLCBOLinksFromFile("D:\\workspace\\crawler\\src\\main\\resources\\lcbo.json");
        parseLCBO(lcbolinks, mongoOperation);*/

        /* load TWE data */
        ArrayList<Link> twelinks = IOUtil.readLCBOLinksFromFile("D:\\workspace\\crawler\\src\\main\\resources\\twe.json");
        parseTWE(twelinks, mongoOperation);
    }

    private static void parseTWE(ArrayList<Link> twelinks, MongoOperations mongoOperation) throws IOException {
        for (Link link: twelinks){
            ArrayList<String> directs = new ArrayList<>();
            ArrayList<Spirit> spirits = new ArrayList<>();
            System.out.println("Start parse category: " + link.getLink());

            ArrayList<Spirit> categories = new ArrayList<>();
            Spirit temp = mongoOperation.findOne(new Query(Criteria.where("_id").is(link.getId())), Spirit.class);
            for (Spirit category: temp.getCategories()){
                Spirit cata = mongoOperation.findOne(new Query(Criteria.where("_id").is(category.getId())), Spirit.class);
                categories.add(cata);
            }
            categories.add(temp);

            int count;
            label: for (count = 1; ; count+=1) {
                try {
                    Document doc = Jsoup
                            .connect(link.getLink() + "?pg=" + count)
                            .timeout(3000)
                            .get();
                    if (doc.select("div.products-wrapper").select("li").text().equals("Your search did not find any products; please change your search criteria.")){
                        doc = Jsoup
                                .connect(link.getLink() + "&pg=" + count)
                                .timeout(3000)
                                .get();
                    }
                    Elements directLinks = doc
                            .select("div.products-wrapper")
                            .select("div.products-grid")
                            .select("div.group-list")
                            .select("div.item")
                            .select("a.product");
                    if (directLinks.size() == 0) {
                        break;
                    }
                    for (Element element: directLinks) {
                        String productURL = "https://www.thewhiskyexchange.com" + element.attr("href");
                        System.out.println("Found link: " + productURL);
                        directs.add(productURL);
                    }
                } catch (Exception e) {
                    count -= 12;
                    continue label;
                }
            }

            label2:for (String directLink: directs) {
                Spirit spirit = new Spirit();
                spirit.setProductURL(directLink);
                Document doc;
                try{
//                    if (spirit.getProductURL().contains("em-vinhas-velhas-reserva")) continue ;
                    doc = Jsoup.connect(spirit.getProductURL()).get();
                } catch (Exception e){
                    continue label2;
                }
                String volume_value;
                try {
                    volume_value = doc.select("span.strength").first().text();
                } catch (Exception e){
                    continue label2;
                }

                String value, volume, name, imageURL;

                int delimeter = volume_value.indexOf("/");
                try {
                    value = volume_value.substring(0, delimeter).replace("cl", "").replace("/", "");
                    volume = volume_value.substring(delimeter + 1).replace("%", "").replace("/", "");
                    name = doc.select("h1[itemprop]").text();
                    imageURL = doc.select("img.lazy").attr("data-original");
                } catch (Exception e){
                    continue label2;
                }

                spirit.setName(name.trim());
                spirit.setVolume(Double.parseDouble(value));
                spirit.setValue(Double.parseDouble(volume));
                spirit.setImageURL(imageURL);

                spirit.setCategories(categories);
                spirit.setId(IOUtil.createTWEID(spirit));
                spirits.add(spirit);

                /*try {
                    URL url = new URL(spirit.getImageURL().replace("https", "http"));
                    InputStream is = url.openStream();
                    OutputStream os = new FileOutputStream("d:\\alcohol_data\\new\\twe\\images\\" + spirit.getId() + ".jpg");
                    byte[] b = new byte[2048];
                    int length;
                    while ((length = is.read(b)) != -1) {
                        os.write(b, 0, length);
                    }
                    is.close();
                    os.close();
                } catch (IOException ioe){
                    System.out.println("skip loading image: " + spirit.getProductURL() + "|" + spirit.getImageURL());
                }*/

                try {
                    URL url = new URL(spirit.getImageURL().replace("https", "http"));
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
                    BufferedImage image = ImageIO.read(connection.getInputStream());
                    File outputfile = new File("d:\\alcohol_data\\new\\twe\\images\\" + spirit.getId() + ".png");
                    ImageIO.write(image, "png", outputfile);
                } catch (Exception e){
                    System.out.println("skip loading image: " + spirit.getProductURL() + "|" + spirit.getImageURL());
                }

                System.out.println("Spirit complete: " + spirit.getProductURL());
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("d:\\alcohol_data\\new\\twe\\" + link.getId() + ".json"), spirits);
        }
    }

    private static void parseLCBO(ArrayList<Link> lcbolinks, MongoOperations mongoOperation) throws IOException {
        for (Link link: lcbolinks){
            ArrayList<String> directs = new ArrayList<>();
            ArrayList<Spirit> spirits = new ArrayList<>();
            System.out.println("Start parse category: " + link.getLink());

            ArrayList<Spirit> categories = new ArrayList<>();
            Spirit temp = mongoOperation.findOne(new Query(Criteria.where("_id").is(link.getId())), Spirit.class);
            for (Spirit category: temp.getCategories()){
                Spirit cata = mongoOperation.findOne(new Query(Criteria.where("_id").is(category.getId())), Spirit.class);
                categories.add(cata);
            }
            categories.add(temp);

            int count;
            label: for (count = 0; ; count = count + 12){
                try {
                    Document doc = Jsoup
                            .connect(link.getLink())
                            .data("contentBeginIndex", "0")
                            .data("productBeginIndex", String.valueOf(count))
                            .data("beginIndex", String.valueOf(count))
                            .data("langId", "-1")
                            .data("requesttype", "ajax")
                            .timeout(3000)
                            .post();
                    Elements directLinks = doc.select("a.category-search-title");
                    if (directLinks.size() == 0) {
                        break;
                    }
                    for (Element element: directLinks) {
                        String productURL = link.getLink() + element.attr("href");
                        int start = productURL.indexOf(".com/lcbo") + 9;
                        int end = productURL.indexOf("/lcbo/product") + 5;
                        String substring = productURL.substring(start, end);
                        String result = productURL.replace(substring, "");
                        directs.add(result);
                    }
                } catch (Exception e) {
                    count -= 12;
                    continue label;
                }
            }

            label2:for (String directLink: directs) {
                Spirit spirit = new Spirit();
                spirit.setProductURL(directLink);
                Document doc;
                try{
                    if (spirit.getProductURL().contains("em-vinhas-velhas-reserva")) continue ;
                    doc = Jsoup.connect(spirit.getProductURL()).get();
                } catch (Exception e){
                    continue label2;
                }
                String volume = doc.select("dt.product-volume").first().text()
                        .replace(" mL bottle", "")
                        .replace(" mL tetra", "")
                        .replace(" mL gift", "")
                        .replace(" mL can", "")
                        .replace(" mL bagnbox", "");
                int x = volume.indexOf("x");
                if (x != -1) {
                    volume = volume.substring(x+1);
                }
                double value = 0;
                Elements values = doc.select("dd");
                for (Element el: values) {
                    if (el.text().contains("%") && (el.text().length() < 9)){
                        value = Double.parseDouble(el.text().replace("%", ""));
                    }
                }
                String imageURL = "http://lcbo.com" + doc.select("div.images").select("img").attr("src");

                spirit.setName(doc.select("h1").first().text().trim());
                spirit.setVolume(value);
                spirit.setValue(Double.parseDouble(volume));
                spirit.setImageURL(imageURL);

                spirit.setCategories(categories);
                spirit.setId(IOUtil.createID(spirit));
                spirits.add(spirit);

                try {
                    URL url = new URL(spirit.getImageURL());
                    InputStream is = url.openStream();
                    OutputStream os = new FileOutputStream("d:\\alcohol_data\\new\\images\\" + spirit.getId() + ".png");
                    byte[] b = new byte[2048];
                    int length;
                    while ((length = is.read(b)) != -1) {
                        os.write(b, 0, length);
                    }
                    is.close();
                    os.close();
                } catch (IOException ioe){
                    System.out.println("skip loading image: " + spirit.getProductURL());
                }

                System.out.println("Spirit complete: " + spirit.getProductURL());
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("d:\\alcohol_data\\new\\" + link.getId() + ".json"), spirits);
        }

    }

    private static void loadBaseLayers(String filepath,  MongoOperations mongoOperation) {
        ArrayList<TempSpirit> layer = IOUtil.readTempSpiritsFromFile(filepath);
        for (TempSpirit tempSpirit: layer){
            Spirit spirit = new Spirit();
            spirit.setId(tempSpirit.getId());
            spirit.setName(tempSpirit.getName());
            spirit.setValue(tempSpirit.getValue());
            spirit.setVolume(tempSpirit.getVolume());

            ArrayList<Spirit> categories = new ArrayList<>();
            for (String sp: tempSpirit.getCategories()){
                Spirit base = mongoOperation.findOne(new Query(Criteria.where("_id").is(sp)), Spirit.class);
                categories.add(base);
            }
            spirit.setCategories(categories);
            mongoOperation.insert(spirit);
        }
    }

}
