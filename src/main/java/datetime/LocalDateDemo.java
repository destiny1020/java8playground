package datetime;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

public class LocalDateDemo {

	@Test
	public void demoDurationBetween() throws InterruptedException {
		Instant start = Instant.now();
		Thread.sleep(1000);
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		long millis = timeElapsed.toMillis();

		System.out.println(millis);

		// all java.time objects are immutable !
		System.out.println(timeElapsed.plus(Duration.ofHours(1)).toMinutes());
	}

	@Test
	public void demoLocalDate() {
		LocalDate today = LocalDate.now();
		System.out.println(today);

		LocalDate birth = LocalDate.of(1987, Month.OCTOBER, 20);
		System.out.println(birth);

		Period livingPeriod = Period.between(birth, today);
		livingPeriod.getUnits().stream().forEach(System.out::println);
		System.out.println(livingPeriod);

		long livingDays = birth.until(today, ChronoUnit.DAYS);
		System.out.println("Lived Days: " + livingDays);

		// test the boundary of until method
		LocalDate birthNext = LocalDate.of(1987, Month.OCTOBER, 21);
		long testUntil = birth.until(birthNext, ChronoUnit.DAYS);
		System.out.println("Should be 1: " + testUntil);

		long daysAhead = today.until(LocalDate.of(2014, Month.DECEMBER, 25),
				ChronoUnit.DAYS);
		System.out.println(daysAhead);

	}

	@Test(expected = DateTimeException.class)
	public void demoInvalidDate() {
		System.out.println(LocalDate.of(2014, Month.FEBRUARY, 30));
	}

	@Test
	public void testPlusMonth() {
		LocalDate dateInFeb = LocalDate.of(2014, Month.JANUARY, 30).plusMonths(
				1);

		// 2014-02-28 instead of 2014-02-30, which is not valid.
		System.out.println(dateInFeb);
	}
}
