package by.seymouriamorpha.smartpourer.crawler;

import by.seymouriamorpha.smartpourer.crawler.entities.SpiritWithBars;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene_Kortelyov on 6/12/2017.
 */
public class Main3 {

    public static void main(String[] args) throws Exception {

        ArrayList<String> links = new ArrayList<>();
        links.add("https://www.proof66.com/liquor/whiskey.html");
        links.add("https://www.proof66.com/liquor/vodka.html");
        links.add("https://www.proof66.com/liquor/brandy.html");
        links.add("https://www.proof66.com/liquor/gin.html");
        links.add("https://www.proof66.com/liquor/rum.html");
        links.add("https://www.proof66.com/liquor/tequila.html");

        ArrayList<SpiritWithBars> spirits = new ArrayList<>();
        ArrayList<SpiritWithBars> result = new ArrayList<>();

        /* get all direct link for category */
        for (String link: links){
            Document doc = Jsoup.connect(link).get();
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

            for (SpiritWithBars spirit: spirits) {
                ArrayList<String> bars = new ArrayList<>();
                ArrayList<String> bars_res = new ArrayList<>();


                Document document = Jsoup.connect(spirit.getProductURL()).get();
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



                }


            }
        }

        /* get all data */

        for (SpiritWithBars spirit: spirits) {
            ArrayList<String> bars = new ArrayList<>();

            Document document = Jsoup.connect(spirit.getProductURL()).get();
            Elements barcodes = document.select("li.list-circle");

            for (Element element: barcodes) {
                bars.add(element.text());
            }
            spirit.setBars(bars);

            for (String bar: spirit.getBars()){
                if (bar.matches("-?\\d+(\\.\\d+)?")){
                    bars.add(bar);
                }
            }


            /*
            if (!bars.isEmpty()) {
                result.add(spirit);
            }
            */
        }

        ArrayList<SpiritWithBars> result1 = new ArrayList<>();

        for (SpiritWithBars spiritWithBars: result){
            ArrayList<String> bars = new ArrayList<>();
            for (String bar: spiritWithBars.getBars()){
                if (bar.matches("-?\\d+(\\.\\d+)?")){
                    bars.add(bar);
                }
            }

            if (!bars.isEmpty()){
                SpiritWithBars withBars = new SpiritWithBars();
                withBars.setBars(bars);
                withBars.setName(spiritWithBars.getName());
                withBars.setProductURL(spiritWithBars.getProductURL());
                result1.add(withBars);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("d:\\proof66.json"), result);

        /*Document doc = Jsoup.connect("https://www.proof66.com/liquor/whiskey.html").get();
        Elements directLinks = doc
                .select("span.font14s480")
                .select("span.font600")
                .select("a");
        for (Element element: directLinks)
        {
            SpiritWithBars spirit = new SpiritWithBars();
            spirit.setName(element.text());
            spirit.setProductURL(element.attr("href").replace("..", "https://proof66.com"));
            spirits.add(spirit);
        }

        for (SpiritWithBars spirit: spirits)
        {
            ArrayList<String> bars = new ArrayList<>();

            Document document = Jsoup.connect(spirit.getProductURL()).get();
            Elements barcodes = document.select("li.list-circle");

            for (Element element: barcodes)
            {
                bars.add(element.text());
            }
            spirit.setBars(bars);

            if (!bars.isEmpty())
            {
                result.add(spirit);
            }
        }*/

        /*ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("d:\\proof66.json"), result);*/

//        spirits.addAll(mapper.readValue(new File("d:\\proof66_whiskey.json"), new TypeReference<List<SpiritWithBars>>(){}));

        /*for (SpiritWithBars spiritWithBars: spirits){
            ArrayList<String> bars = new ArrayList<>();
            for (String bar: spiritWithBars.getBars()){
                if (bar.matches("-?\\d+(\\.\\d+)?")){
                    bars.add(bar);
                }
            }

            if (!bars.isEmpty()){
                SpiritWithBars withBars = new SpiritWithBars();
                withBars.setBars(bars);
                withBars.setName(spiritWithBars.getName());
                withBars.setProductURL(spiritWithBars.getProductURL());
                result.add(withBars);
            }
        }

        mapper.writeValue(new File("d:\\proof66_whiskey_new.json"), result);*/

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
