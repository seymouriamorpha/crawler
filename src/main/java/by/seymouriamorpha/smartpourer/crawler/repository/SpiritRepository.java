package by.seymouriamorpha.smartpourer.crawler.repository;

import by.seymouriamorpha.smartpourer.crawler.entities.Spirit;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Eugene_Kortelyov on 5/31/2017.
 */
public interface SpiritRepository extends MongoRepository<Spirit, String> {

    Spirit findById(String firstName);
    List<Spirit> findByName(String lastName);

}
