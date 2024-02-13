import dto.OrderCreateRequest;
import generator.OrderGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static config.Config.BASE_URI;
import static config.Config.ORDER_CREATE_API;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;


@RunWith(Parameterized.class)
public class CreateOrderParametrTest {
    private OrderCreateRequest orderCreateRequest;
    private static OrderGenerator generator;
    public CreateOrderParametrTest(OrderCreateRequest orderCreateRequest) {
        this.orderCreateRequest = orderCreateRequest;
    }

    @Parameterized.Parameters
    public static Object[][] getOrder() {
        return new Object[][]{
                {new OrderCreateRequest(generator.firstName(),generator.lastName(),generator.address(),generator.metroStation(),generator.phone(),generator.rentTime(),generator.deliveryDate(),generator.comment(), null)},
                {new OrderCreateRequest(generator.firstName(),generator.lastName(),generator.address(),generator.metroStation(),generator.phone(),generator.rentTime(),generator.deliveryDate(),generator.comment(), new String[]{})},
                {new OrderCreateRequest(generator.firstName(),generator.lastName(),generator.address(),generator.metroStation(),generator.phone(),generator.rentTime(),generator.deliveryDate(),generator.comment(), new String[]{"BLACK"})},
                {new OrderCreateRequest(generator.firstName(),generator.lastName(),generator.address(),generator.metroStation(),generator.phone(),generator.rentTime(),generator.deliveryDate(),generator.comment(), new String[]{"GREY"})},
                {new OrderCreateRequest(generator.firstName(),generator.lastName(),generator.address(),generator.metroStation(),generator.phone(),generator.rentTime(),generator.deliveryDate(),generator.comment(), new String[]{"GREY", "BLACK"})}
        };
    }

    @Before
    public void setUp() {

    }

    @Step("Creating an order")
    public Response createOrder(OrderCreateRequest orderCreateRequest) {
        return given().baseUri(BASE_URI).contentType(ContentType.JSON).body(orderCreateRequest).when().post(ORDER_CREATE_API);
    }
    @Step("Checking the result of successful order creation")
    public void checkSuccessfulCreation(Response response) {
        response.then().statusCode(201).assertThat().body("track",notNullValue());
    }

    @Test
    @DisplayName("Create an order")
    @Description("You can create an order with different color options")
    public void createOrderShouldReturnTrackId() {
        Response response = createOrder(orderCreateRequest);
        checkSuccessfulCreation(response);
    }
}
