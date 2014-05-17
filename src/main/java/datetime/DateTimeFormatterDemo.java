package datetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.junit.Test;

public class DateTimeFormatterDemo {

	private final LocalDateTime now = LocalDateTime.now();

	@Test
	public void demoStandardFormatters() {

		String formatted = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(now);
		System.out.println(formatted);
	}

	@Test
	public void demoLocaleSpecificFormatters() {
		System.out.println(DateTimeFormatter.ofLocalizedDateTime(
				FormatStyle.LONG).format(now));
	}

	@Test
	public void demoCustomPatternFormatters() {
		DateTimeFormatter formatter = DateTimeFormatter
				.ofPattern("E yyyy-MM-dd HH:mm");
		System.out.println(formatter.format(now));
	}
}
