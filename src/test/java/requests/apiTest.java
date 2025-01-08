package requests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class apiTest {

    @Test
public void GetTest() {
    String baseUrl = "https://petstore.swagger.io/v2/pet/9756";
    Response response = given().when().get(baseUrl);
    Assert.assertEquals(response.getStatusCode(), 200);
    System.out.println(" Status Code = " + response.statusCode());
    Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 200 OK");
    System.out.println(" Status Line = " + response.statusLine());
    Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
    System.out.println(" Content Type = " + response.contentType());
    Assert.assertTrue(response.getTime() <= 5000, "Response time exceeded 5 seconds");
    System.out.println(" Response Time = " + response.getTime());
    Assert.assertTrue(response.body().asString().contains("köpek"));
    System.out.println(" Response Body = " + response.body().asString());
    int id = response.jsonPath().getInt("id");
    Assert.assertEquals(id, 9756, "ID doesn't match");
    response.prettyPrint();
}
    @Test
    public void PostTest(){
        String requestBody = """
        {
            "id": 9756,
            "category": {
                "id": 0,
                "name": "cesur"
            },
            "name": "köpek",
            "photoUrls": [
                "string"
            ],
            "tags": [
                {
                    "id": 0,
                    "name": "alman kurdu"
                }
            ],
            "status": "available"
        }
        """;
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/pet");

        Assert.assertEquals(response.statusCode(), 200, "Status code mismatch!");
        Assert.assertEquals(response.jsonPath().getInt("id"), 9756, "ID mismatch!");
        Assert.assertEquals(response.jsonPath().getString("category.name"), "cesur", "Category name mismatch!");
        Assert.assertEquals(response.jsonPath().getString("name"), "köpek", "Pet name mismatch!");
        Assert.assertEquals(response.jsonPath().getString("status"), "available", "Status mismatch!");
        response.prettyPrint();
    }
    @Test
    public void PutTest() {
    RestAssured.baseURI = "https://petstore.swagger.io/v2";
    String requestBody = """
    {
        "id": 9756,
        "category": {
            "id": 0,
            "name": "pamuk"
        },
        "name": "kedi",
        "photoUrls": [
            "string"
        ],
        "tags": [
            {
                "id": 0,
                "name": "tekir"
            }
        ],
        "status": "sold"
    }
    """;

    // Send PUT request
    Response response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .put("/pet");

    // Assertions
    Assert.assertEquals(response.statusCode(), 200, "Status code mismatch!");
    Assert.assertEquals(response.jsonPath().getInt("id"), 9756, "ID mismatch!");
    Assert.assertEquals(response.jsonPath().getString("category.name"), "pamuk", "Category name mismatch!");
    Assert.assertEquals(response.jsonPath().getString("name"), "kedi", "Pet name mismatch!");
    Assert.assertEquals(response.jsonPath().getString("status"), "sold", "Status mismatch!");
    Assert.assertEquals(response.jsonPath().getString("tags[0].name"), "tekir", "Tag name mismatch!");

    // Response Logging (Optional)
    response.prettyPrint();
    }

    public class deletePet{
        }

        @Test
        public void deletePet() {
            RestAssured.baseURI = "https://petstore.swagger.io/v2";
            // ID of the pet to delete
            int petId = 9756;

            // Send DELETE request
            Response response = RestAssured.given()
                    .delete("/pet/" + petId);

            // Assertions
            Assert.assertEquals(response.statusCode(), 200, "Status code mismatch!");
            Assert.assertEquals(response.jsonPath().getString("message"), String.valueOf(petId), "Delete confirmation message mismatch!");

            // Verify that the pet has been deleted
            Response getResponse = RestAssured.given()
                    .get("/pet/" + petId);

            Assert.assertEquals(getResponse.statusCode(), 404, "Pet was not deleted!");
        }
    }




































