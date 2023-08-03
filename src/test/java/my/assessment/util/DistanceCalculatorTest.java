package my.assessment.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DistanceCalculatorTest {

  private final DistanceCalculator distanceCalculator = new DistanceCalculator();

  @Nested
  class CalculateTest {

    @Test
    void successfullyCalculatesDistance() {
      double value = distanceCalculator.calculate(12.34, 18.75, -10.25, 81.1234);
      assertThat(value).isEqualTo(9711.032638674393);
    }

  }

}