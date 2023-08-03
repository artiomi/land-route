package my.assessment.repository;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import my.assessment.entity.Country;
import my.assessment.util.CountriesResourceParser;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class InMemoryCountryRepository implements CountryRepository {

  static final Map<String, Country> COUNTRIES = new HashMap<>();

  private final CountriesResourceParser countriesResourceParser;


  public InMemoryCountryRepository(CountriesResourceParser countriesResourceParser) {
    this.countriesResourceParser = countriesResourceParser;
  }

  @PostConstruct
  void postConstruct() {
    Map<String, Country> entities = countriesResourceParser.getCountries();
    COUNTRIES.putAll(entities);
  }

  @Override
  public Optional<Country> findById(String id) {
    return Optional.ofNullable(COUNTRIES.get(id));
  }

}
