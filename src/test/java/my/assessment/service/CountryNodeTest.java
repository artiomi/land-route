package my.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import my.assessment.entity.Country;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CountryNodeTest {

  @Nested
  class InitTest {

    @Test
    void successfullyInitializesNewNode() {
      Country countryStub = Country.builder().id("ARG").build();
      CountryNode countryNode = CountryNode.init(countryStub, 1.23);

      assertThat(countryNode.getCountry()).isEqualTo(countryStub);
      assertThat(countryNode.getTraversedCountries()).isEqualTo(List.of("ARG"));
      assertThat(countryNode.getDistancesSum()).isEqualTo(1.23);
      assertThat(countryNode.getMinDistanceToEnd()).isEqualTo(1.23);
      assertThat(countryNode.getDistanceFromStart()).isEqualTo(0);
    }
  }

  @Nested
  class CreateFromPrevTest {

    CountryNode prevNode = CountryNode.init(Country.builder().id("ARG").build(), 2.34);

    @Test
    void successfullyCreatesNewNode() {
      Country countryStub = Country.builder().id("BRA").build();
      CountryNode countryNode = CountryNode.createFromPrev(countryStub, prevNode, 100.25, 25.17);

      assertThat(countryNode.getCountry()).isEqualTo(countryStub);
      assertThat(countryNode.getTraversedCountries()).isEqualTo(List.of("ARG", "BRA"));
      assertThat(countryNode.getDistancesSum()).isEqualTo(125.42);
      assertThat(countryNode.getMinDistanceToEnd()).isEqualTo(100.25);
      assertThat(countryNode.getDistanceFromStart()).isEqualTo(25.17);
    }


  }

}