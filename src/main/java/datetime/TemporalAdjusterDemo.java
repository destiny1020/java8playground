package datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import org.junit.Test;

public class TemporalAdjusterDemo {

	@Test
	public void demoFirstFridayNextMonth() {
		// cal the first friday in next month
		LocalDate today = LocalDate.now();
		LocalDate firstFridayInNextMonth = LocalDate.of(today.getYear(),
				today.getMonthValue() + 1, 1).with(
				TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

		System.out.println(firstFridayInNextMonth);
	}

	/**
	 * Demo how to get the last working day in the previous month.
	 */
	@Test
	public void demoLastWorkingDayPreviousMonth() {
		// define the lambda, passed in date should be today
		TemporalAdjuster LAST_WORKING_DAY_IN_PREVIOUS_MONTH = w -> {
			LocalDate result = (LocalDate) w;
			result = result.minusMonths(1).with(
					TemporalAdjusters.lastDayOfMonth());
			while (result.getDayOfWeek().getValue() >= 6) {
				result = result.minusDays(1);
			}
			return result;
		};

		LocalDate lastWorkingDayInPreviousMonth = LocalDate.now().with(
				LAST_WORKING_DAY_IN_PREVIOUS_MONTH);

		System.out.println(lastWorkingDayInPreviousMonth);
	}

	/**
	 * Demo how to get the first working day in the next month.
	 */
	@Test
	public void demoFirstWorkingDayNextMonth() {
		// define the lambda, passed in the date should be today
		TemporalAdjuster FIRST_WORKING_DAY_IN_NEXT_MONTH = w -> {
			LocalDate result = (LocalDate) w;
			result = result.plusMonths(1).with(
					TemporalAdjusters.firstDayOfMonth());
			while (result.getDayOfWeek().getValue() >= 6) {
				result = result.plusDays(1);
			}
			return result;
		};

		LocalDate firstWorkingDayInNextMonth = LocalDate.now().with(
				FIRST_WORKING_DAY_IN_NEXT_MONTH);

		System.out.println(firstWorkingDayInNextMonth);
	}
}
