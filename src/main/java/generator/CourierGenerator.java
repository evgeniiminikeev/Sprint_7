package generator;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {
    public static String firstName() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static String password() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static String login() {
        return RandomStringUtils.randomAlphabetic(10);
    }
}
