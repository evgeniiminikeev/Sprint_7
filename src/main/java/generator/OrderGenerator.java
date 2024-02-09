package generator;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class OrderGenerator {
    public static String firstName() {
        return RandomStringUtils.randomAlphabetic(10);
    }
    public static String lastName() {
        return RandomStringUtils.randomAlphabetic(10);
    }
    public static String address() {
        return RandomStringUtils.randomAlphabetic(20);
    }
    public static String metroStation() {
        return RandomStringUtils.randomAlphabetic(20);
    }
    public static String phone() {
        return RandomStringUtils.randomAlphabetic(10);
    }
    public static int rentTime() {
        Random random = new Random();
        return random.nextInt(14) + 1;
    }
    public static String deliveryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date minDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        Date maxDate = calendar.getTime();
        long randomDay = ThreadLocalRandom.current().nextLong(minDate.getTime(), maxDate.getTime());
        Date randomDate = new Date(randomDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(randomDate);
    }
    public static String comment() {
        return RandomStringUtils.randomAlphabetic(30);
    }
}
