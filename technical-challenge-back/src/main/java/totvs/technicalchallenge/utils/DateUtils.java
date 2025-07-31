package totvs.technicalchallenge.utils;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtils {

  public List<LocalDate> generateDates(final LocalDate initialDate,
                                       final LocalDate finalDate,
                                       final LocalDate firstPaymentDate,
                                       final int TOTAL_INSTALLMENTS) {
    List<LocalDate> dates = new ArrayList<>();
    dates.add(initialDate);

    LocalDate currentDate = initialDate;
    for (int i = 0; i < TOTAL_INSTALLMENTS; i++) {
      LocalDate monthEnd = currentDate.with(TemporalAdjusters.lastDayOfMonth());
      if (!dates.contains(monthEnd)) {
        dates.add(monthEnd);
      }

      LocalDate nextMonth = currentDate.plusMonths(1);
      LocalDate nextPayment = (i == TOTAL_INSTALLMENTS - 1) ?
          finalDate :
          nextMonth.withDayOfMonth(15);

      if (i == 0) {
        nextPayment = firstPaymentDate;
      }

      dates.add(nextPayment);
      currentDate = nextMonth;
    }
    return dates;
  }
}
