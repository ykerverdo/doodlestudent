package fr.istic.tlc.fuzzing;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import fr.istic.tlc.dao.PollRepository;

import java.util.stream.Stream;

public class PollRepositoryFuzzTest {

    // Generate random inputs for findBySlug method
    static Stream<String> findBySlugInputs() {
        return Stream.generate(() -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                char randomChar = (char) ((Math.random() * 26) + 'a'); // Random lowercase letter
                sb.append(randomChar);
            }
            return sb.toString();
        }).limit(1000);
    }

    @ParameterizedTest
    @MethodSource("findBySlugInputs")
    public void testFindBySlug(String slug) {
        try {
            System.out.println("Testing findBySlug with slug: " + slug);
            PollRepository repository = new PollRepository();
            repository.findBySlug(slug); // Assuming this method is mocked or connected to a test DB
        } catch (Exception e) {
            System.err.println("Exception for slug: " + slug);
            e.printStackTrace();
        }
    }
}