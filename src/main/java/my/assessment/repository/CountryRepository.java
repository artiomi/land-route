package my.assessment.repository;

import java.util.Optional;
import my.assessment.entity.Country;

public interface CountryRepository {

  Optional<Country> findById(String id);
}
