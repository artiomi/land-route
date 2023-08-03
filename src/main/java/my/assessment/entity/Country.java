package my.assessment.entity;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Country {

  String id;
  String name;
  String region;
  double lat;
  double lon;
  List<String> borders;
}
