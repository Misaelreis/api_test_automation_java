package rest;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class VerbsTest {
    @Test
    public void deveSalvarUsuario(){
        given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body("{\"name\":\"Josy\",\"age\":25}")
                .when()
                    .post("http://restapi.wcaquino.me/users")
                .then()
                    .log().all()
                    .statusCode(201)
                    .body("id", is(notNullValue()))
                    .body("name", is("Josy"))
                    .body("age", is(25))
        ;
    }

    @Test
    public void naoDeveSalvarUsuarioSemNome(){
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\"age\":25}")
                .when()
                .post("http://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(400)
                .body("id", is(nullValue()))
                .body("error", is("Name é um atributo obrigatório"))
        ;
    }

    @Test
    public void deveSalvarUsuarioXML() {
        given()
                .log().all()
                .contentType(ContentType.XML)
                .body("<user><name>Josy</name><age>25</age></user>")
                .when()
                .post("http://restapi.wcaquino.me/usersXML")
                .then()
                .log().all()
                .statusCode(201)
                .body("user.@id", is(notNullValue()))
                .body("user.name", is("Josy"))
                .body("user.age", is("25"))
        ;
    }

    @Test
    public void deveAlterarUsuario(){
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Josy\",\"age\":25}")
                .when()
                .put("http://restapi.wcaquino.me/users/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("name", is("Josy"))
                .body("age", is(25))
                .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void deveCustomizarUrl(){
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Josy\",\"age\":25}")
                .when()
                .put("http://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("name", is("Josy"))
                .body("age", is(25))
                .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void deveCustomizarUrlParte2(){
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Josy\",\"age\":25}")
                .pathParam("entidade", "users")
                .pathParam("userId", "1")
                .when()
                .put("http://restapi.wcaquino.me/{entidade}/{userId}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("name", is("Josy"))
                .body("age", is(25))
                .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void deveRemoverUsuario(){
        given()
                .log().all()
                .when()
                    .delete("http://restapi.wcaquino.me/users/1")
                .then()
                    .log().all()
                    .statusCode(204)
        ;
    }

    @Test
    public void NaoDeveRemoveruUsarioInexistente(){
        given()
                .log().all()
                .when()
                .delete("http://restapi.wcaquino.me/users/166")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", is("Registro inexistente"))
        ;
    }

    @Test
    public void deveSalvarUsuarioComMap(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "UsuarioMap");
        params.put("age", 25);
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when()
                .post("http://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("UsuarioMap"))
                .body("age", is(25))
        ;
    }

    @Test
    public void deveSalvarUsuarioComObjeto(){
        User user = new User("Object", 25);
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("http://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Object"))
                .body("age", is(25))
        ;
    }

    @Test
    public void deveDeserializarObjetoAoSalvarUsuario(){
        User user = new User("ObjectDes", 25);
        User usuarioInserido = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("http://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(User.class)
        ;
        System.out.println(usuarioInserido);
        Assert.assertEquals("ObjectDes", usuarioInserido.getName());
        Assert.assertThat(usuarioInserido.getAge(), is(25));
        Assert.assertThat(usuarioInserido.getId(), is(notNullValue()));
    }

    @Test
    public void deveSalvarUsuarioXMLUsandoObjeto() {
        User user = new User("UserXML", 25);
        given()
                .log().all()
                .contentType(ContentType.XML)
                .body(user)
                .when()
                .post("http://restapi.wcaquino.me/usersXML")
                .then()
                .log().all()
                .statusCode(201)
                .body("user.@id", is(notNullValue()))
                .body("user.name", is("UserXML"))
                .body("user.age", is("25"))
        ;
    }

    @Test
    public void deveDeserializarObjetoAoSalvarUsuarioXML() {
        User user = new User("UserXML", 25);
        User usuarioInserido = given()
                .log().all()
                .contentType(ContentType.XML)
                .body(user)
                .when()
                .post("http://restapi.wcaquino.me/usersXML")
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(User.class)
        ;
        Assert.assertThat(usuarioInserido.getId(), is(notNullValue()));
        Assert.assertThat(usuarioInserido.getName(), is("UserXML"));
        Assert.assertThat(usuarioInserido.getAge(), is(25));
        Assert.assertThat(usuarioInserido.getSlary(), is(nullValue()));
    }
}
