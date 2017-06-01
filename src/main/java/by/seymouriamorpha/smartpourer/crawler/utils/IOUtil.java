package by.seymouriamorpha.smartpourer.crawler.utils;

import by.seymouriamorpha.smartpourer.crawler.entities.Spirit;
import by.seymouriamorpha.smartpourer.crawler.entities.TempSpirit;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene_Kortelyov on 5/31/2017.
 */
public class IOUtil {

    public static ArrayList<Spirit> readSpiritsFromFile(String filepath) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(filepath),  new TypeReference<List<Spirit>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<TempSpirit> readTempSpiritsFromFile(String filepath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(filepath),  new TypeReference<List<TempSpirit>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
