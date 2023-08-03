package my.assessment.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import my.assessment.entity.Country;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

class CountriesResourceParserTest {

  private static final String JSON_VALID = "[{\"name\":{\"common\":\"Aruba\"},\"cca3\":\"ABW\",\"region\":\"Americas\""
      + ",\"latlng\":[12.5,-69.96666666],\"borders\":[]},{\"name\":{\"common\":\"Afghanistan\"},\"cca3\":\"AFG\","
      + "\"region\":\"Asia\",\"latlng\":[33,65],\"borders\":[\"IRN\",\"PAK\",\"TKM\",\"UZB\",\"TJK\",\"CHN\"]}]";
  private static final String JSON_NULL_NAME = "[{\"name\":{\"common\":null},\"cca3\":\"ABW\",\"region\":\"Americas\","
      + "\"latlng\":[12.5,-69.96666666],\"borders\":[]}]";
  private static final String JSON_NULL_CCA3 =
      "[{\"name\":{\"common\":\"Aruba\"},\"cca3\":null,\"region\":\"Americas\","
          + "\"latlng\":[12.5,-69.96666666],\"borders\":[]}]";
  private static final String JSON_NULL_REGION = "[{\"name\":{\"common\":\"Aruba\"},\"cca3\":\"ABW\",\"region\":null,"
      + "\"latlng\":[12.5,-69.96666666],\"borders\":[]}]";
  private static final String JSON_NULL_LATLNG =
      "[{\"name\":{\"common\":\"Aruba\"},\"cca3\":\"ABW\",\"region\":\"Americas\",\"latlng\":null,\"borders\":[]}]";
  private static final String JSON_WRONG_LENGTH_LATLNG =
      "[{\"name\":{\"common\":\"Aruba\"},\"cca3\":\"ABW\",\"region\":\"Americas\",\"latlng\":[12.5],\"borders\":[]}]";

  private static final String JSON_NULL_BORDERS =
      "[{\"name\":{\"common\":\"Aruba\"},\"cca3\":\"ABW\",\"region\":\"Americas\",\"latlng\":[12.5,-69.96666666],\"borders\":null}]";


  private final ObjectMapper objectMapper = new ObjectMapper();
  private CountriesResourceParser countriesResourceParser;


  private void initParser(String json) {
    Resource countriesResource = new ByteArrayResource(json.getBytes());
    countriesResourceParser = new CountriesResourceParser(objectMapper, countriesResource);
  }

  @Nested
  class GetCountriesTest {

    @Test
    void parseCountriesSuccessfully() {
      initParser(JSON_VALID);
      Map<String, Country> countries = countriesResourceParser.getCountries();
      assertThat(countries).hasSize(2);
    }

    @ParameterizedTest
    @CsvSource(value = {
        JSON_NULL_NAME + "|name can't be blank",
        JSON_NULL_CCA3 + "|id can't be blank",
        JSON_NULL_REGION + "|region can't be null",
        JSON_NULL_LATLNG + "|latlng should contain exactly 2 values.",
        JSON_WRONG_LENGTH_LATLNG + "|latlng should contain exactly 2 values.",
        JSON_NULL_BORDERS + "|borders can't be null",
    }, delimiter = '|')
    void failsToParseJson(String inputJson, String errorMessage) {
      initParser(inputJson);
      assertThatThrownBy(() -> countriesResourceParser.getCountries())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage(errorMessage);
    }
  }

}