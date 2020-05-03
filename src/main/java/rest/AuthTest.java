package rest;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

public class AuthTest {
    @Test
    public void deveAcessarSWAPI(){
        given()
                    .log().all()
                .when()
                    .get("https://swapi.dev/api/people/1")
                .then()
                    .statusCode(200)
                    .log().all()
                    .body("name", is("Luke Skywalker"))
                ;
    }
}
