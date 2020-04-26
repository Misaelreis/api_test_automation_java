package rest;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OlaMundoTest {
    @Test
    public void testOlaMundo (){
        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertTrue("O status code deveria ser 200.",response.getStatusCode() == 200);
        Assert.assertEquals(200, response.statusCode());
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }

    @Test
    public void conhecendoRestAssured (){
        //Exemplo1
        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);

        //Exemplo2
        get("http://restapi.wcaquino.me/ola").then().statusCode(200);

        //Exemplo3
        given()
                .when()
                    .get("http://restapi.wcaquino.me/ola")
                .then()
                    .statusCode(200);
    }

    @Test
    public void conhecendoMatchesComHamcrest(){
        Assert.assertThat("Maria", Matchers.is("Maria"));
        Assert.assertThat(128, Matchers.is(128));
        Assert.assertThat(128, Matchers.isA(Integer.class));
        Assert.assertThat(123d, Matchers.isA(Double.class));
        Assert.assertThat(123d, Matchers.greaterThan(120d));
        Assert.assertThat(120, Matchers.lessThan(121));

        List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
        assertThat(impares, hasSize(5));
        assertThat(impares, contains(1,3,5,7,9));
        assertThat(impares, containsInAnyOrder(1,5,3,9,7));
        assertThat(impares, hasItems(1,3));
        assertThat(impares, hasItem(1));

        assertThat("Maria", is(not("João")));
        assertThat("Maria", anyOf(is("Maria"), is("João")));
        assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("quina"), containsString("qui")));
    }

    @Test
    public void validandoBody(){
        given()
                .when()
                .get("http://restapi.wcaquino.me/ola")
                .then()
                .statusCode(200)
                .body(is("Ola Mundo!"))
                .body(containsString("Mundo"))
                .body(is(not(nullValue())));
    }
}
