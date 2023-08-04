## Route calculation app
This project calculates route between two countries following land crossing border using [A* search algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm).<br>
Project exposes endpoint `GET /routing/{from}/{to}`, where `from` and `to` are [ISO 3166-1 alpha-3](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) countries codes.<br>
#### Example
Request: `curl -X 'GET' 'http://localhost:8080/routing/PRT/SWE'  -H 'accept: application/json' `<br>
Will return response: 
```
{
  "route": ["PRT", "ESP", "FRA", "DEU", "POL", "RUS", "FIN", "SWE"]
}
```
### Build
Java version - `17`
```
mvn clean compile
```

### Run
```
mvn spring-boot:run
```
* [Swagger documentation](http://localhost:8080/swagger-ui/index.html)
