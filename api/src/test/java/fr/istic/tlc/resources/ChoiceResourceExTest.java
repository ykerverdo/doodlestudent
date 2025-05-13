package fr.istic.tlc.resources;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.istic.tlc.dao.ChoiceRepository;
import fr.istic.tlc.dao.PollRepository;
import fr.istic.tlc.dao.UserRepository;
import fr.istic.tlc.domain.Choice;
import fr.istic.tlc.domain.Poll;
import fr.istic.tlc.domain.User;
import jakarta.persistence.EntityManager;

public class ChoiceResourceExTest {

    private ChoiceResourceEx choiceResourceEx;

    @Mock
    private ChoiceRepository choiceRepository;

    @Mock
    private PollRepository pollRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        choiceResourceEx = new ChoiceResourceEx();
        choiceResourceEx.choiceRepository = choiceRepository;
        choiceResourceEx.pollRepository = pollRepository;
        choiceResourceEx.userRepository = userRepository;

        // Mock the EntityManager
        when(choiceRepository.getEntityManager()).thenReturn(entityManager);
        when(userRepository.getEntityManager()).thenReturn(entityManager);
    }

    @Test
    public void testRemoveVote_Success() {
        // Mock data
        String slug = "test-slug";
        long idChoice = 1L;
        long idUser = 1L;

        Poll poll = mock(Poll.class);
        Choice choice = mock(Choice.class);
        User user = mock(User.class);

        when(pollRepository.findBySlug(slug)).thenReturn(poll);
        when(choiceRepository.findById(idChoice)).thenReturn(choice);
        when(userRepository.findById(idUser)).thenReturn(user);
        when(poll.getPollChoices()).thenReturn(List.of(choice));
        when(user.getUserChoices()).thenReturn(List.of(choice));

        // Call the method
        ResponseEntity<Object> response = choiceResourceEx.removeVote(slug, idChoice, idUser);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(choice).removeUser(user);
        verify(entityManager).merge(choice); // Verify merge is called on the mocked EntityManager
        verify(user).removeChoice(choice);
        verify(entityManager).merge(user); // Verify merge is called on the mocked EntityManager
    }

    @Test
    public void testRemoveVote_PollNotFound() {
        // Mock data
        String slug = "test-slug";
        long idChoice = 1L;
        long idUser = 1L;

        when(pollRepository.findBySlug(slug)).thenReturn(null);

        // Call the method
        ResponseEntity<Object> response = choiceResourceEx.removeVote(slug, idChoice, idUser);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveVote_ChoiceNotFound() {
        // Mock data
        String slug = "test-slug";
        long idChoice = 1L;
        long idUser = 1L;

        Poll poll = mock(Poll.class);

        when(pollRepository.findBySlug(slug)).thenReturn(poll);
        when(choiceRepository.findById(idChoice)).thenReturn(null);

        // Call the method
        ResponseEntity<Object> response = choiceResourceEx.removeVote(slug, idChoice, idUser);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveVote_UserNotFound() {
        // Mock data
        String slug = "test-slug";
        long idChoice = 1L;
        long idUser = 1L;

        Poll poll = mock(Poll.class);
        Choice choice = mock(Choice.class);

        when(pollRepository.findBySlug(slug)).thenReturn(poll);
        when(choiceRepository.findById(idChoice)).thenReturn(choice);
        when(userRepository.findById(idUser)).thenReturn(null);

        // Call the method
        ResponseEntity<Object> response = choiceResourceEx.removeVote(slug, idChoice, idUser);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveVote_ChoiceNotInPoll() {
        // Mock data
        String slug = "test-slug";
        long idChoice = 1L;
        long idUser = 1L;

        Poll poll = mock(Poll.class);
        Choice choice = mock(Choice.class);
        User user = mock(User.class);

        when(pollRepository.findBySlug(slug)).thenReturn(poll);
        when(choiceRepository.findById(idChoice)).thenReturn(choice);
        when(userRepository.findById(idUser)).thenReturn(user);
        when(poll.getPollChoices()).thenReturn(new ArrayList<>());

        // Call the method
        ResponseEntity<Object> response = choiceResourceEx.removeVote(slug, idChoice, idUser);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveVote_UserDidNotVoteForChoice() {
        // Mock data
        String slug = "test-slug";
        long idChoice = 1L;
        long idUser = 1L;

        Poll poll = mock(Poll.class);
        Choice choice = mock(Choice.class);
        User user = mock(User.class);

        when(pollRepository.findBySlug(slug)).thenReturn(poll);
        when(choiceRepository.findById(idChoice)).thenReturn(choice);
        when(userRepository.findById(idUser)).thenReturn(user);
        when(poll.getPollChoices()).thenReturn(List.of(choice));
        when(user.getUserChoices()).thenReturn(new ArrayList<>());

        // Call the method
        ResponseEntity<Object> response = choiceResourceEx.removeVote(slug, idChoice, idUser);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}