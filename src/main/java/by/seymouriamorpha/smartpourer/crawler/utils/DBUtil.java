package by.seymouriamorpha.smartpourer.crawler.utils;

import by.seymouriamorpha.smartpourer.crawler.entities.Spirit;
import by.seymouriamorpha.smartpourer.crawler.repository.SpiritRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * @author Eugene_Kortelyov on 5/31/2017.
 */
public class DBUtil {

    @Autowired
    private SpiritRepository spiritRepository;

    public void saveSpirits(ArrayList<Spirit> spirits) {
        spiritRepository.save(spirits);
        System.out.println("complete");
    }

}
