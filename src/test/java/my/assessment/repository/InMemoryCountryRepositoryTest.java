package my.assessment.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Map;
import my.assessment.entity.Country;
import my.assessment.util.CountriesResourceParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InMemoryCountryRepositoryTest {

  private static final String TEST_ID = "id1";
  @Mock
  private CountriesResourceParser countriesResourceParser;
  private InMemoryCountryRepository inMemoryCountryRepository;

  @BeforeEach
  void setUp() {
    InMemoryCountryRepository.COUNTRIES.clear();
    inMemoryCountryRepository = new InMemoryCountryRepository(countriesResourceParser);
  }

  @Nested
  class FindByIdTest {

    Country countryStub = Country.builder().id(TEST_ID).build();

    @Test
    void findExistingCountry() {
      InMemoryCountryRepository.COUNTRIES.put(TEST_ID, countryStub);
      assertThat(inMemoryCountryRepository.findById(TEST_ID)).contains(countryStub);
    }

    @Test
    void returnsEmptyOptionalForMissingCountry() {
      assertThat(inMemoryCountryRepository.findById(TEST_ID)).isEmpty();
    }
  }

  @Nested
  class PostConstructTest {

    @Test
    void successfullyLoadsCountries() {
      Country countryStub = Country.builder().id(TEST_ID).build();
      when(countriesResourceParser.getCountries()).thenReturn(Map.of(TEST_ID, countryStub));
      assertThat(InMemoryCountryRepository.COUNTRIES.isEmpty()).isTrue();
      inMemoryCountryRepository.postConstruct();
      assertThat(InMemoryCountryRepository.COUNTRIES).containsEntry(TEST_ID, countryStub);
    }
  }
}