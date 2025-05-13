package fr.istic.tlc.resources;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.istic.tlc.dao.PollRepository;
import fr.istic.tlc.domain.Poll;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

public class PollResourceExFuzzTest {

    private PollResourceEx pollResourceEx;

    @Mock
    private PollRepository pollRepository;

    private Random random;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pollResourceEx = new PollResourceEx();
        pollResourceEx.pollRepository = pollRepository;
        random = new Random();
    }

    @Test
    public void testRetrieveAllPolls_Fuzz() {
        // Mock data
        List<Poll> mockPolls = List.of(mock(Poll.class), mock(Poll.class));
        PanacheQuery<Poll> mockQuery = mock(PanacheQuery.class);
        when(mockQuery.list()).thenReturn(mockPolls);

        // CORRECTION ICI
        when(pollRepository.findAll(any(Sort.class))).thenReturn(mockQuery);

        // Call the method
        ResponseEntity<List<Poll>> response = pollResourceEx.retrieveAllpolls();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPolls, response.getBody());
    }


    @Test
    public void testRetrievePoll_Fuzz() {
        // Generate random slug and token
        String slug = generateRandomString(10);
        String token = generateRandomString(15);

        // Mock data
        Poll poll = mock(Poll.class);
        when(pollRepository.findBySlug(slug)).thenReturn(poll);
        when(poll.getSlugAdmin()).thenReturn(token);

        // Call the method
        ResponseEntity<Poll> response = pollResourceEx.retrievePoll(slug, token);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRetrievePoll_InvalidSlug() {
        // Generate random slug and token
        String slug = generateRandomString(10);
        String token = generateRandomString(15);

        // Mock data
        when(pollRepository.findBySlug(slug)).thenReturn(null);

        // Call the method
        ResponseEntity<Poll> response = pollResourceEx.retrievePoll(slug, token);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRetrievePoll_InvalidToken() {
        // Generate random slug and token
        String slug = generateRandomString(10);
        String token = generateRandomString(15);

        // Mock data
        Poll poll = mock(Poll.class);
        when(pollRepository.findBySlug(slug)).thenReturn(poll);
        when(poll.getSlugAdmin()).thenReturn("invalid-token");

        // Call the method
        ResponseEntity<Poll> response = pollResourceEx.retrievePoll(slug, token);

        // Verify
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testRetrievePadURL_Fuzz() {
        // Generate random slug
        String slug = generateRandomString(10);

        // Mock data
        Poll poll = mock(Poll.class);
        when(pollRepository.findBySlug(slug)).thenReturn(poll);
        when(poll.getPadURL()).thenReturn("http://example.com/pad");

        // Call the method
        ResponseEntity<String> response = pollResourceEx.retrievePadURL(slug);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("http://example.com/pad", response.getBody());
    }

    @Test
    public void testRetrievePadURL_InvalidSlug() {
        // Generate random slug
        String slug = generateRandomString(10);

        // Mock data
        when(pollRepository.findBySlug(slug)).thenReturn(null);

        // Call the method
        ResponseEntity<String> response = pollResourceEx.retrievePadURL(slug);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private String generateRandomString(int length) {
        return random.ints(97, 123) // ASCII range for lowercase letters
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
