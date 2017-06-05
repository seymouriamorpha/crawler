package by.seymouriamorpha.smartpourer.crawler;

import by.seymouriamorpha.smartpourer.crawler.entities.Spirit;
import by.seymouriamorpha.smartpourer.crawler.utils.IOUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author Eugene_Kortelyov on 6/5/2017.
 */
public class Main2 {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File folder = new File("d:\\alcohol_data\\newnew");
        File[] listOfFiles = folder.listFiles();

        ArrayList<Spirit> spirits = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                spirits.addAll(mapper.readValue(new File("d:\\alcohol_data\\newnew\\" + listOfFiles[i].getName()),  new TypeReference<List<Spirit>>(){}));
            }
        }

        System.out.println(spirits.size());

        List<Spirit> result = spirits.stream()
                .filter(object ->{
                    Optional<Spirit> spirit = spirits.stream()
                            .filter(o -> o.getName().equals(object.getName()))
                            .max(Comparator.comparingInt(o -> o.getCategories().size()));
                    return spirit.get().equals(object);
                }).collect(Collectors.toList());

        System.out.println(result.size());

        mapper.writeValue(new File("d:\\alcohol_data\\05.06.2017\\all_lcbo_data.json"), spirits);

    }

}
