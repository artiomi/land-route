package my.assessment.exception;

public class RouteCalculationException extends RuntimeException {

  public RouteCalculationException(String message) {
    super(message);
  }

  public static RouteCalculationException countryNotFound(String countryId) {
    return new RouteCalculationException(String.format("Country with id:[%s] not found.", countryId));
  }

  public static RouteCalculationException routeNotFound(String fromCountryId, String toCountryId) {
    return new RouteCalculationException(
        String.format("Unable to find route between [%s] and [%s].", fromCountryId, toCountryId));
  }

  public static RouteCalculationException missingBorders(String countryId) {
    return new RouteCalculationException(
        String.format("Country [%s] doesn't have common borders with other countries.", countryId));
  }
}
