package my.assessment.service;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import my.assessment.entity.Country;

@Getter
@ToString
public class CountryNode implements Comparable<CountryNode> {

  private final Country country;
  private final List<String> traversedCountries = new ArrayList<>();
  private final double distanceFromStart;
  private final double minDistanceToEnd;
  private final double distancesSum;

  private CountryNode(Country country, double distanceFromStart, double minDistanceToEnd) {
    this.country = country;
    this.distanceFromStart = distanceFromStart;
    this.minDistanceToEnd = minDistanceToEnd;
    this.distancesSum = this.distanceFromStart + minDistanceToEnd;
  }


  public static CountryNode init(Country country, double minDistanceToEnd) {
    CountryNode node = new CountryNode(country, 0d, minDistanceToEnd);
    node.traversedCountries.add(country.getId());

    return node;
  }

  public static CountryNode createFromPrev(Country country, CountryNode prev, double minDistanceToEnd,
      double distanceFromPrevCountry) {
    CountryNode node = new CountryNode(country, prev.getDistanceFromStart() + distanceFromPrevCountry,
        minDistanceToEnd);
    node.traversedCountries.addAll(prev.getTraversedCountries());
    node.traversedCountries.add(country.getId());

    return node;
  }

  public List<String> getTraversedCountries() {
    return new ArrayList<>(this.traversedCountries);
  }

  @Override
  public int compareTo(CountryNode other) {
    return (int) (this.getDistancesSum() - other.getDistancesSum());
  }
}
