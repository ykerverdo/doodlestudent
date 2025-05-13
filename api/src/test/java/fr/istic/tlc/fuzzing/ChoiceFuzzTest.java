package fr.istic.tlc.fuzzing;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import fr.istic.tlc.domain.Choice;

import java.util.Date;
import java.util.stream.Stream;

public class ChoiceFuzzTest {

    // Generate random inputs for setStartDate and setEndDate methods
    static Stream<Date> dateInputs() {
        return Stream.generate(() -> new Date((long) (Math.random() * System.currentTimeMillis()))).limit(1000);
    }

    @ParameterizedTest
    @MethodSource("dateInputs")
    public void testSetStartDate(Date date) {
        try {
            Choice choice = new Choice();
            choice.setstartDate(date);
            System.out.println("Testing setStartDate with date: " + date);
        } catch (Exception e) {
            System.err.println("Exception for date: " + date);
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("dateInputs")
    public void testSetEndDate(Date date) {
        try {
            Choice choice = new Choice();
            choice.setendDate(date);
            System.out.println("Testing setEndDate with date: " + date);
        } catch (Exception e) {
            System.err.println("Exception for date: " + date);
            e.printStackTrace();
        }
    }
}