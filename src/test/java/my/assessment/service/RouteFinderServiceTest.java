package my.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import my.assessment.exception.RouteCalculationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RouteFinderServiceTest {

  @Autowired
  private RouteFinderService routeFinderService;

  @Nested
  class CalculateRouteTest {

    @Test
    void calculateRouteSuccessful() {
      List<String> expectedResult = List.of("MYS", "THA", "MMR", "IND", "PAK", "IRN", "IRQ", "SAU", "OMN");
      List<String> result = routeFinderService.calculateRoute("MYS", "OMN");
      assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void failsToCalculateRouteForNullFromCountry() {
      assertThatThrownBy(() -> routeFinderService.calculateRoute(null, "OMN"))
          .isInstanceOf(RouteCalculationException.class)
          .hasMessage("Parameters from & to should not be blank.");
    }

    @Test
    void failsToCalculateRouteForNullToCountry() {
      assertThatThrownBy(() -> routeFinderService.calculateRoute("OMN", null))
          .isInstanceOf(RouteCalculationException.class)
          .hasMessage("Parameters from & to should not be blank.");
    }

    @Test
    void failsToCalculateRouteForNonExistingFromCountry() {
      assertThatThrownBy(() -> routeFinderService.calculateRoute("MISSING", "OMN"))
          .isInstanceOf(RouteCalculationException.class)
          .hasMessage("Country with id:[MISSING] not found.");
    }

    @Test
    void failsToCalculateRouteForNonExistingToCountry() {
      assertThatThrownBy(() -> routeFinderService.calculateRoute("OMN", "MISSING"))
          .isInstanceOf(RouteCalculationException.class)
          .hasMessage("Country with id:[MISSING] not found.");
    }

    @Test
    void failsToCalculateRouteForFromCountryWithoutBorders() {
      assertThatThrownBy(() -> routeFinderService.calculateRoute("ABW", "OMN"))
          .isInstanceOf(RouteCalculationException.class)
          .hasMessage("Country [ABW] doesn't have common borders with other countries.");
    }

    @Test
    void failsToCalculateRouteForToCountryWithoutBorders() {
      assertThatThrownBy(() -> routeFinderService.calculateRoute("OMN", "ABW"))
          .isInstanceOf(RouteCalculationException.class)
          .hasMessage("Country [ABW] doesn't have common borders with other countries.");
    }

    @Test
    void failsToCalculateRouteForCountriesWithoutLandBorders() {
      assertThatThrownBy(() -> routeFinderService.calculateRoute("OMN", "GBR"))
          .isInstanceOf(RouteCalculationException.class)
          .hasMessage("Unable to find route between [OMN] and [GBR].");
    }

    @Test
    void failsToCalculateRouteForCountriesFromDifferentRegions() {
      assertThatThrownBy(() -> routeFinderService.calculateRoute("OMN", "CHL"))
          .isInstanceOf(RouteCalculationException.class)
          .hasMessage(
              "Countries OMN and CHL are located in regions: Asia and Americas, which don't have common borders.");
    }
  }

}