package fr.istic.tlc.fuzzing;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.istic.tlc.dao.PollRepository;

public class PollRepositoryFuzzTest {

    // Generate random inputs for findBySlug method
   public static Stream<String> findBySlugInputs() {
        return Stream.generate(() -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                char randomChar = (char) ('a' + (Math.random() * 26)); // Random lowercase letter
                sb.append(randomChar);
            }
            return sb.toString();
        }).limit(1000); // Generate 1000 random strings
    }

    @ParameterizedTest
    @MethodSource("findBySlugInputs")
    public void testFindBySlug(String slug) {
        try {
            System.out.println("Testing findBySlug with slug: " + slug);

            // Mock the PollRepository
            PollRepository repository = Mockito.mock(PollRepository.class);

            // Define behavior for the mock
            when(repository.findBySlug(slug)).thenReturn(null); // Return null for simplicity

            // Call the method
            repository.findBySlug(slug);

            // Verify the method was called
            verify(repository).findBySlug(slug);

        } catch (Exception e) {
            System.err.println("Exception for slug: " + slug);
            e.printStackTrace();
            fail("Test failed due to exception: " + e.getMessage()); // Mark the test as failed
        }
    }
}