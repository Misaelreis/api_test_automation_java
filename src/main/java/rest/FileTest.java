package rest;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class FileTest {
    @Test
    public void deveObrigarEnvioArquivo(){
        given()
                    .log().all()
                .when()
                    .post("http://restapi.wcaquino.me/upload")
                .then()
                    .log().all()
                    .statusCode(404)
                    .body("error", is("Arquivo não enviado"))
        ;
    }

    @Test
    public void deveFazerUploadDoArquivo(){
        given()
                .log().all()
                .multiPart("arquivo", new File("src/main/resources/users.pdf"))
                .when()
                .post("http://restapi.wcaquino.me/upload")
                .then()
                .log().all()
                .time(lessThan(5000L))
                .statusCode(200)
                .body("name", is("users.pdf"))
        ;
    }

    @Test
    public void deveBaixarArquivo() throws IOException {
        byte[] image = given()
                    .log().all()
                .when()
                    .get("http://restapi.wcaquino.me/download")
                .then()
                    .statusCode(200)
                    .extract().asByteArray()
        ;
        File imagem = new File("src/main/resources/file.jpg");
        OutputStream out = new FileOutputStream(imagem);
        out.write(image);
        out.close();

        Assert.assertThat(imagem.length(), lessThan(100000L));
    }
}
