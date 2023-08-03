package my.assessment.util;

import static my.assessment.exception.RouteCalculationException.missingBorders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import my.assessment.entity.Country;
import my.assessment.exception.RouteCalculationException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RouteFinderValidator {

  private static final List<String> AFR_EURASIA = List.of("Asia", "Europe", "Africa");
  private static final Map<String, List<String>> ALLOWED_REGIONS = new HashMap<>();

  static {
    ALLOWED_REGIONS.put("Europe", AFR_EURASIA);
    ALLOWED_REGIONS.put("Asia", AFR_EURASIA);
    ALLOWED_REGIONS.put("Africa", AFR_EURASIA);
    ALLOWED_REGIONS.put("Americas", List.of("Americas"));
    ALLOWED_REGIONS.put("Antarctic", List.of("Antarctic"));
    ALLOWED_REGIONS.put("Oceania", List.of("Oceania"));
  }


  public void validate(Country from, Country to) {
    if (from.getId().equals(to.getId())) {
      throw new RouteCalculationException(
          String.format("Can't find route for same country %s and %s.", from.getId(), to.getId()));
    }
    if (CollectionUtils.isEmpty(from.getBorders())) {
      throw missingBorders(from.getId());
    }
    if (CollectionUtils.isEmpty(to.getBorders())) {
      throw missingBorders(to.getId());
    }
    List<String> mappedRegions = ALLOWED_REGIONS.get(from.getRegion());
    if (mappedRegions == null || !mappedRegions.contains(to.getRegion())) {
      String message = String.format(
          "Countries %s and %s are located in regions: %s and %s, which don't have common borders.",
          from.getId(), to.getId(), from.getRegion(), to.getRegion());
      throw new RouteCalculationException(message);
    }

  }

}
