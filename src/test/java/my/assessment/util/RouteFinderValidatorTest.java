package my.assessment.util;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import my.assessment.entity.Country;
import my.assessment.exception.RouteCalculationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RouteFinderValidatorTest {

  private final RouteFinderValidator routeFinderValidator = new RouteFinderValidator();

  @Nested
  class ValidateTest {

    @Test
    void throwsExceptionIfFromAndToAreSameCountry() {
      Country from = Country.builder().id("test").build();
      Country to = Country.builder().id("test").build();
      assertThatThrownBy(() -> routeFinderValidator.validate(from, to))
          .isInstanceOf(RouteCalculationException.class)
          .hasMessage("It's not allowed to have similar from: test and to: test countries.");
    }

    @Test
    void throwsExceptionIfFromCountryHasNoBorders() {
      Country from = Country.builder().id("test").build();
      Country to = Country.builder().id("test2").build();
      assertThatThrownBy(() -> routeFinderValidator.validate(from, to))
          .isInstanceOf(RouteCalculationException.class)
          .hasMessage("Country [test] doesn't have common borders with other countries.");
    }

    @Test
    void throwsExceptionIfToCountryHasNoBorders() {
      Country from = Country.builder().id("test").borders(List.of("someBorder")).build();
      Country to = Country.builder().id("test2").build();
      assertThatThrownBy(() -> routeFinderValidator.validate(from, to))
          .isInstanceOf(RouteCalculationException.class)
          .hasMessage("Country [test2] doesn't have common borders with other countries.");
    }

    @ParameterizedTest
    @CsvSource({
        "Europe, Europe",
        "Europe, Asia",
        "Europe, Africa",
        "Asia,   Europe",
        "Asia,   Asia",
        "Asia,   Africa",
        "Africa, Europe",
        "Africa, Asia",
        "Africa, Africa",
        "Americas,Americas",
        "Antarctic, Antarctic",
        "Oceania, Oceania"
    })
    void successPassValidationIfCountriesAreFromRelatedRegions(String fromRegion, String toRegion) {
      Country from = Country.builder().id("test").borders(List.of("someBorder")).region(fromRegion).build();
      Country to = Country.builder().id("test2").borders(List.of("someBorder")).region(toRegion).build();
      assertThatNoException().isThrownBy(() -> routeFinderValidator.validate(from, to));
    }

    @ParameterizedTest
    @CsvSource({
        "Europe, Americas",
        "Europe, Antarctic",
        "Europe, Oceania",

        "Asia,  Americas",
        "Asia,  Antarctic",
        "Asia,  Oceania",

        "Africa, Americas",
        "Africa, Antarctic",
        "Africa, Oceania",

        "Americas, Europe",
        "Americas, Asia",
        "Americas, Africa",
        "Americas, Antarctic",
        "Americas, Oceania",

        "Antarctic,   Europe",
        "Antarctic,   Asia",
        "Antarctic,   Africa",
        "Antarctic, Americas",
        "Antarctic, Oceania",

        "Oceania,   Europe",
        "Oceania,   Asia",
        "Oceania,   Africa",
        "Oceania, Americas",
        "Oceania, Antarctic",

    })
    void throwsExceptionIfCountriesAreFromNoTRelatedRegions(String fromRegion, String toRegion) {
      Country from = Country.builder().id("test").borders(List.of("someBorder")).region(fromRegion).build();
      Country to = Country.builder().id("test2").borders(List.of("someBorder")).region(toRegion).build();
      assertThatThrownBy(() -> routeFinderValidator.validate(from, to)).isInstanceOf(RouteCalculationException.class);
    }
  }

}