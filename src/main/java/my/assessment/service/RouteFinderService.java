package my.assessment.service;

import static my.assessment.exception.RouteCalculationException.countryNotFound;
import static my.assessment.exception.RouteCalculationException.routeNotFound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import lombok.extern.slf4j.Slf4j;
import my.assessment.entity.Country;
import my.assessment.exception.RouteCalculationException;
import my.assessment.repository.CountryRepository;
import my.assessment.util.DistanceCalculator;
import my.assessment.util.RouteFinderValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RouteFinderService {

  private final CountryRepository countryRepository;
  private final DistanceCalculator distanceCalculator;
  private final RouteFinderValidator routeFinderValidator;

  public RouteFinderService(CountryRepository countryRepository, DistanceCalculator distanceCalculator,
      RouteFinderValidator routeFinderValidator) {
    this.countryRepository = countryRepository;
    this.distanceCalculator = distanceCalculator;
    this.routeFinderValidator = routeFinderValidator;
  }

  public List<String> calculateRoute(String from, String to) throws RouteCalculationException {
    if (StringUtils.isAnyBlank(from, to)) {
      throw new RouteCalculationException("Parameters from & to should not be blank.");
    }
    log.info("Calculate route from:{} to:{}.", from, to);

    Country fromCountry = countryRepository.findById(from).orElseThrow(() -> countryNotFound(from));
    Country toCountry = countryRepository.findById(to).orElseThrow(() -> countryNotFound(to));
    routeFinderValidator.validate(fromCountry, toCountry);

    Map<String, Double> visitedCountries = new HashMap<>();
    visitedCountries.put(fromCountry.getId(), 0D);

    PriorityQueue<CountryNode> queue = new PriorityQueue<>();
    double initDistance = distanceCalculator.calculate(fromCountry.getLat(), toCountry.getLat(), fromCountry.getLon(),
        toCountry.getLon());
    queue.add(CountryNode.init(fromCountry, initDistance));

    while (!queue.isEmpty()) {
      CountryNode currentNode = queue.poll();

      if (currentNode.getCountry().getId().equals(toCountry.getId())) {
        log.info("Path from {} to {} found. Distance: {} km.", from, to, currentNode.getDistanceFromStart());
        return currentNode.getTraversedCountries();
      }

      log.info("------Iterate border countries for: {}----------", currentNode.getCountry().getId());
      for (var nextCountryId : currentNode.getCountry().getBorders()) {
        log.info("Check distance between:{} and: {} ", currentNode.getCountry().getId(), nextCountryId);

        CountryNode nextNode = getNextNode(nextCountryId, currentNode, toCountry);
        if (visitedCountries.getOrDefault(nextCountryId, Double.MAX_VALUE) > nextNode.getDistancesSum()) {
          queue.add(nextNode);
          visitedCountries.put(nextCountryId, nextNode.getDistancesSum());
          log.info("Queuing node from:{}, to:{}, minDistToEnd:{}, distFromStart:{}, distancesSum:{}",
              nextNode.getCountry().getId(), toCountry.getId(), nextNode.getMinDistanceToEnd(),
              nextNode.getDistanceFromStart(), nextNode.getDistancesSum());
        }
      }
    }
    throw routeNotFound(from, to);
  }

  private CountryNode getNextNode(String nextCountryId, CountryNode currentNode, Country toCountry) {
    Country nextCountry = countryRepository.findById(nextCountryId).orElseThrow(() -> countryNotFound(nextCountryId));
    double distanceFromParent = distanceCalculator.calculate(currentNode.getCountry().getLat(), nextCountry.getLat(),
        currentNode.getCountry().getLon(), nextCountry.getLon());
    double minDistanceToTarget = distanceCalculator.calculate(nextCountry.getLat(), toCountry.getLat(),
        nextCountry.getLon(), toCountry.getLon());
    return CountryNode.createFromPrev(nextCountry, currentNode, minDistanceToTarget,
        distanceFromParent);
  }

}
