package fr.istic.tlc.fuzzing;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import fr.istic.tlc.services.SendEmail;

public class SendEmailFuzzTest {

    // Génération des inputs sous forme de Stream<Arguments>
    static Stream<Arguments> getICS1Inputs() {
        return Stream.generate(() -> {
            Date start = new Date((long) (Math.random() * System.currentTimeMillis()));
            Date end = new Date(start.getTime() + (long) (Math.random() * 1000000));
            String libelle = "Meeting " + (int) (Math.random() * 100);
            List<String> attendees = List.of("attendee" + (int) (Math.random() * 10) + "@example.com");
            String organizer = "organizer@example.com";
            return Arguments.of(start, end, libelle, attendees, organizer);
        }).limit(1000);
    }

    @ParameterizedTest
    @MethodSource("getICS1Inputs")
    public void testGetICS1(Date start, Date end, String libelle, List<String> attendees, String organizer) {
        try {
            SendEmail sendEmail = new SendEmail();
            String result = sendEmail.getICS1(start, end, libelle, attendees, organizer);
            System.out.printf("Testing getICS1 with: %s, %s, %s, %s, %s%n", start, end, libelle, attendees, organizer);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.err.printf("Exception for inputs: %s, %s, %s, %s, %s%n", start, end, libelle, attendees, organizer);
            e.printStackTrace();
        }
    }
}
