package rest;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void deveObterClima(){
        given()
                    .log().all()
                    .queryParam("q", "Serro,BR")
                    .queryParam("appid", "459a2acfe99e840d160adb8baf606abd")
                    .queryParam("units", "metrics")
                .when()
                    .get("http://api.openweathermap.org/data/2.5/weather")
                .then()
                    .statusCode(200)
                    .body("name", is("Serro"))
                    .body("coord.lon", is(-43.38f))
                    .body("main.temp", greaterThan(291f))
                    .log().all()
                ;
    }

    @Test
    public void naoDeveAcessarSemSenha(){
        given()
                    .log().all()
                .when()
                    .get("http://restapi.wcaquino.me/basicauth")
                .then()
                    .statusCode(401)
                    .log().all()
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasica(){
        given()
                .log().all()
                .when()
                .get("http://admin:senha@restapi.wcaquino.me/basicauth")
                .then()
                .statusCode(200)
                .body("status", is("logado"))
                .log().all()
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasica2(){
        given()
                .log().all()
                .auth().basic("admin", "senha")
                .when()
                .get("http://restapi.wcaquino.me/basicauth")
                .then()
                .statusCode(200)
                .body("status", is("logado"))
                .log().all()
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasicaChallenge(){
        given()
                .log().all()
                .auth().preemptive().basic("admin", "senha")
                .when()
                .get("http://restapi.wcaquino.me/basicauth2")
                .then()
                .statusCode(200)
                .body("status", is("logado"))
                .log().all()
        ;
    }

    @Test
    public void deveFazerAutenticacaoComTokenJWT(){
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "zael.au@hotmail.com");
        login.put("senha", "test");
        //login na api e obter o token
        String token = given()
                    .log().all()
                    .body(login)
                    .contentType(ContentType.JSON)
                .when()
                    .post("http://barrigarest.wcaquino.me/signin")
                .then()
                    .statusCode(200)
                    .extract().path("token");
        //Obter as contas
        given()
                    .log().all()
                    .header("Authorization","JWT " + token)
                .when()
                    .get("http://barrigarest.wcaquino.me/contas")
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("nome", hasItem("Conta de teste"))
                ;
    }

    @Test
    public void deveAcessarAplicacaoWeb(){
        String cookie = given()
                    .log().all()
                    .formParam("email","zael.au@hotmail.com")
                    .formParam("senha", "test")
                    .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .when()
                    .post("http://seubarriga.wcaquino.me/logar")
                .then()
                    .statusCode(200)
                    .log().all()
                    .extract().header("set-cookie")
                ;

        cookie = cookie.split("=")[1].split(";")[0];
        String body = given()
                    .log().all()
                    .cookie("connect.sid", cookie)
                .when()
                    .get("http://seubarriga.wcaquino.me/contas")
                .then()
                    .statusCode(200)
                    .body("html.body.table.tbody.tr[0].td[0]", is("Conta de teste"))
                    .extract().body().asString()
                ;

        System.out.println("----------------------------------------------");
        XmlPath xmlPath = new XmlPath(XmlPath.CompatibilityMode.HTML, body);
        System.out.println(xmlPath.get("html.body.table.tbody.tr[0].td[0]"));
    }
}
