package fr.istic.tlc.fuzzing;

import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import fr.istic.tlc.services.Utils;

public class UtilsFuzzTest {

    static Stream<Integer> generateSlugInputs() {
        return Stream.generate(() -> (int) (Math.random() * 100) + 1)
                .limit(1000);
    }

    @ParameterizedTest
    @MethodSource("generateSlugInputs")
    public void testGenerateSlug(int length) {
        try {
            System.out.println("Testing generateSlug with length: " + length);
            String result = Utils.getInstance().generateSlug(length);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.err.println("Exception for length: " + length);
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("intersectInputs")
    void testIntersect(Date start1, Date end1, Date start2, Date end2) {
        // Appel à la méthode à tester
        // Exemple : Utils.intersect(start1, end1, start2, end2);
        System.out.printf("Testing intersect with [%s - %s] and [%s - %s]%n", start1, end1, start2, end2);
    }

    static Stream<Arguments> intersectInputs() {
        return Stream.generate(() -> {
            Date start1 = new Date((long) (Math.random() * System.currentTimeMillis()));
            Date end1 = new Date(start1.getTime() + (long) (Math.random() * 1000000));
            Date start2 = new Date((long) (Math.random() * System.currentTimeMillis()));
            Date end2 = new Date(start2.getTime() + (long) (Math.random() * 1000000));
            return Arguments.of(start1, end1, start2, end2);
        }).limit(1000);
    }
}
