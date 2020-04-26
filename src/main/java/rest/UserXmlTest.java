package rest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserXmlTest {
    public static ResponseSpecification resSpec;
    public static RequestSpecification reqSpec;
    @BeforeClass
    public static void setup(){
        baseURI = "http://restapi.wcaquino.me";

        RequestSpecBuilder reqBuild = new RequestSpecBuilder();
        reqBuild.log(LogDetail.ALL);
        reqSpec = reqBuild.build();

        ResponseSpecBuilder resBuild = new ResponseSpecBuilder();
        resBuild.expectStatusCode(200);
        resSpec = resBuild.build();

        requestSpecification = reqSpec;
        responseSpecification = resSpec;
    }
    @Test
    public void devoTrabalharComXml (){
        given()
                .when()
                    .get("/usersXML/3")
                .then()

                    .rootPath("user")
                    .body("name", is("Ana Julia"))
                    .body("@id", is("3"))

                    .rootPath("user.filhos")
                    .body("name.size()", is(2))

                    .rootPath("filhos")
                    .body("filhos.name[0]", is("Zezinho"))
                    .body("filhos.name[1]", is("Luizinho"))
                    .appendRootPath("filhos")
                    .body("name", hasItem("Zezinho"))
                    .body("name", hasItems("Zezinho", "Luizinho"))
        ;
    }

    @Test
    public void validacaoAvancadaComXml(){
        given()
                .when()
                    .get("/usersXML")
                .then()
                    .body("users.user.size()", is(3))
                    .body("users.user.findAll{it.age.toInteger()<=25}.size()", is(2))
                    .body("users.user.@id", hasItems("1", "2", "3"))
                    .body("users.user.find{it.age == 25}.name", is("Maria Joaquina"))
                    .body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
                    .body("users.user.salary.find{it != null}.toDouble()", is(1234.5678))
                    .body("users.user.age.collect{it.toInteger()*2}", hasItems(40, 50, 60))
                    .body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"))
        ;
    }

    @Test
    public void validacaoAvancadaComXmlJava(){
        ArrayList<NodeImpl> nomes = given()
                .when()
                    .get("/usersXML")
                .then()
                    .extract().path("users.user.name.findAll{it.toString().contains('n')}");
        Assert.assertEquals(2, nomes.size());
        Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
        Assert.assertTrue("Ana Julia".equalsIgnoreCase(nomes.get(1).toString()));
    }

    @Test
    public void validacaoAvancadaComXPath(){
        given()
                .when()
                    .get("/usersXML")
                .then()
                    .body(hasXPath("count(/users/user)", is("3")))
                    .body(hasXPath("/users/user[@id = '1']"))
                    .body(hasXPath("//user[@id = '2']"))
                    .body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
                    .body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"), containsString("Luizinho"))))
                    .body(hasXPath("/users/user/name", is("João da Silva")))
                    .body(hasXPath("//name", is("João da Silva")))
                    .body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
                    .body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
                    .body(hasXPath("count(/users/user/name[contains(.,'n')])", is("2")))
                    .body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
                    .body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
                    .body(hasXPath("//user[age > 20][age < 30]/name", is("Maria Joaquina")))
        ;
    }
}
