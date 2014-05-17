package datetime;

import java.time.LocalTime;

import org.junit.Test;

public class LocalTimeDemo {

	@Test
	public void demoLocalTime() {
		LocalTime now = LocalTime.now();
		System.out.println(now);

		LocalTime noon = LocalTime.of(12, 0);
		System.out.println(noon);

		System.out.println(now.plusHours(1));
	}

}
