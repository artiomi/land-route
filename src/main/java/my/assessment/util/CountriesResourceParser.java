package my.assessment.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import my.assessment.entity.Country;
import my.assessment.entity.Country.CountryBuilder;
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
      JsonNode jsonNode = objectMapper.readTree(countriesResource.getContentAsByteArray());
      Iterator<JsonNode> nodes = jsonNode.elements();
      while (nodes.hasNext()) {
        JsonNode node = nodes.next();
        Country country = buildInstance(node);
        result.put(country.getId(), country);
      }
      log.info("Countries load complete. Loaded {} entries.", result.size());
    } catch (IOException e) {
      throw new RuntimeException(String.format("File [%s] parsing failed", countriesResource.getFilename()), e);
    }
    return result;
  }

  private Country buildInstance(JsonNode node) throws IOException {
    String id = node.get("cca3").textValue();
    String name = node.get("name").get("common").textValue();
    String region = node.get("region").textValue();
    List<String> borders = objectMapper.readValue(node.get("borders").traverse(), new TypeReference<>() {
    });
    List<Double> latLng = objectMapper.readValue(node.get("latlng").traverse(), new TypeReference<>() {
    });
    validate(id, name, region, borders, latLng);
    CountryBuilder countryBuilder = Country.builder()
        .id(id)
        .region(region)
        .name(name)
        .borders(borders)
        .lat(latLng.get(0))
        .lon(latLng.get(1));

    return countryBuilder.build();

  }

  private void validate(String id, String name, String region, List<String> borders, List<Double> latLng) {
    if (StringUtils.isBlank(id)) {
      throw new IllegalArgumentException("id can't be blank");
    }
    if (StringUtils.isBlank(name)) {
      throw new IllegalArgumentException("name can't be blank");
    }
    if (StringUtils.isBlank(region)) {
      throw new IllegalArgumentException("region can't be null");
    }
    if (borders == null) {
      throw new IllegalArgumentException("borders can't be null");
    }
    if (latLng == null || latLng.size() != 2) {
      throw new IllegalArgumentException("latlng should contain exactly 2 values.");
    }
  }


}
