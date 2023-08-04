package my.assessment.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import my.assessment.entity.Country;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CountriesResourceParser {

  private final ObjectMapper objectMapper;
  private final Resource countriesResource;

  public CountriesResourceParser(ObjectMapper objectMapper,
      @Value("${countries-file-path}") Resource countriesResource) {
    this.objectMapper = objectMapper;
    this.countriesResource = countriesResource;
  }

  public Map<String, Country> getCountries() {
    log.info("Start loading countries from file:{}", countriesResource.getFilename());
    Map<String, Country> result = new HashMap<>();
    try {
      List<CountryRecord> coutryRecords = objectMapper.readValue(
          countriesResource.getContentAsByteArray(), new TypeReference<>() {
          });
      for (var record : coutryRecords) {
        validate(record);
        Country country = Country.builder()
            .id(record.id)
            .region(record.region)
            .name(record.countryName)
            .borders(record.borders)
            .lat(record.latlng.get(0))
            .lon(record.latlng.get(1))
            .build();
        result.put(country.getId(), country);
      }

      log.info("Countries load complete. Loaded {} entries.", result.size());
    } catch (IOException e) {
      throw new RuntimeException(String.format("File [%s] parsing failed", countriesResource.getFilename()), e);
    }
    return result;
  }

  private void validate(CountryRecord record) {
    if (StringUtils.isBlank(record.id)) {
      throw new IllegalArgumentException("id can't be blank");
    }
    if (StringUtils.isBlank(record.countryName)) {
      throw new IllegalArgumentException("name can't be blank");
    }
    if (StringUtils.isBlank(record.region)) {
      throw new IllegalArgumentException("region can't be null");
    }
    if (record.borders == null) {
      throw new IllegalArgumentException("borders can't be null");
    }
    if (record.latlng == null || record.latlng.size() != 2) {
      throw new IllegalArgumentException("latlng should contain exactly 2 values.");
    }
  }

  @Setter
  @NoArgsConstructor
  static class CountryRecord {

    @JsonProperty("cca3")
    private String id;
    private String countryName;
    @JsonProperty("region")
    private String region;
    @JsonProperty("latlng")
    private List<Double> latlng;
    @JsonProperty("borders")
    private List<String> borders;

    @JsonProperty("name")
    public void populateName(Map<String, Object> name) {
      if (name.get("common") != null) {
        this.countryName = String.valueOf(name.get("common"));
      }
    }

  }

}
