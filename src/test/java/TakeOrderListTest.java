import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static config.Config.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TakeOrderListTest {

    @Before
    public void setUp() {

    }

    @Step("Getting an orders")
    public Response getOrders() {
        return given().baseUri(BASE_URI).contentType(ContentType.JSON).when().get(ORDER_GET_API);
    }
    @Step("Checking get orders")
    public void checkGetOrders(Response response) {
        response.then().statusCode(200).assertThat().body("orders",notNullValue());
    }

    @Test
    @DisplayName("Check getting a list of orders")
    @Description("A non-empty list of orders must be received")
    public void takeOrderListShouldReturnListOfAllOrders() {
        Response response = getOrders();
        checkGetOrders(response);
    }
}
