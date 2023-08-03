package my.assessment.controller;

import jakarta.validation.constraints.Pattern;
import java.util.List;
import my.assessment.service.RouteFinderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Validated
@RestController
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RouteController {

  private final RouteFinderService routeFinderService;

  public RouteController(RouteFinderService routeFinderService) {
    this.routeFinderService = routeFinderService;
  }

  @GetMapping("routing/{from}/{to}")
  public ResponseEntity<RouteResponse> calculateRoute(
      @PathVariable @Pattern(regexp = "^[A-Z]{3}$", message = "are allow A-Z characters with length 3") String from,
      @PathVariable @Pattern(regexp = "^[A-Z]{3}$", message = "are allow A-Z characters with length 3") String to) {

    List<String> route = routeFinderService.calculateRoute(from, to);
    return ResponseEntity.ok(new RouteResponse(route));
  }

}
