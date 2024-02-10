import dto.CourierCreateRequest;
import dto.CourierDeleteRequest;
import dto.CourierLoginRequest;
import generator.CourierGenerator;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static config.Config.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


public class LoginCourierTest {

    private CourierGenerator generator = new CourierGenerator();
    String login = generator.login();
    String password = generator.password();
    String firstName = generator.firstName();

    @Before
    public void setUp() {

    }

    @Step("Creating a courier, sending a Post request to /api/v1/courier")
    public Response createCourier(String login, String password, String firstName) {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);
        return given().baseUri(BASE_URI).contentType(ContentType.JSON).body(courierCreateRequest).when().post(COURIER_CREATE_API);
    }
    @Step("Checking the result of successful courier authorization")
    public void checkSuccessfulAuthorization(String login, String password) {
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest(login, password);
        Response response = given().baseUri(BASE_URI).contentType(ContentType.JSON).body(courierLoginRequest).when().post(COURIER_LOGIN_API);
        response.then().statusCode(200).assertThat().body("id",notNullValue());
    }
    @Step("Checking the result of failed courier authorization")
    public void checkFailedAuthorization(String login, String password, int status, String errorMessage) {
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest(login, password);
        Response response = given().baseUri(BASE_URI).contentType(ContentType.JSON).body(courierLoginRequest).when().post(COURIER_LOGIN_API);
        response.then().statusCode(status).assertThat().body("message",equalTo(errorMessage));
    }
    @Step("Get courier's id")
    public String getCourierId(String login, String password) {
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest(login, password);
        return given().baseUri(BASE_URI).contentType(ContentType.JSON).body(courierLoginRequest).when().post(COURIER_LOGIN_API).then().extract().path("id").toString();
    }
    @Step("Delete created courier")
    public void deleteCourier(String courierId) {
        given().baseUri(BASE_URI).contentType(ContentType.JSON).body(new CourierDeleteRequest(courierId)).when().delete(COURIER_DELETE_API + courierId);
    }


    @Test
    @DisplayName("Successful authorization")
    @Description("The courier can log in, a successful request will return the courier id")
    public void successfulAuthorizationShouldReturnId() {
        createCourier(login, password, firstName);
        checkSuccessfulAuthorization(login, password);
    }

    @Test
    @DisplayName("Login with incorrect password")
    @Description("Logging in with an incorrect password results in a 404 error")
    public void authorizationWithWrongPasswordShouldReturnFALSE() {
        String wrongPassword = generator.password();
        createCourier(login, password, firstName);
        checkFailedAuthorization(login, wrongPassword, 404, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Request for id with non-existent login-password pair")
    @Description("Authorization with a non-existent login and password results in a 404 error")
    public void requestIdOfNonExistentCourierShouldReturnError404() {
        checkFailedAuthorization(login, password, 404, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Authorization without login")
    @Description("Authorization without login results in error 400")
    public void requestIdWithoutLoginShouldReturnError400() {
        checkFailedAuthorization(null, password, 400, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Authorization without password")
    @Description("Authorization without login results in error 400")
    public void requestIdWithoutPasswordShouldReturnError400() {
        checkFailedAuthorization(login, null, 400, "Недостаточно данных для входа");
    }


    @After
    public void tearDown(){
        try {
            String id = getCourierId(login,password);
            deleteCourier(id);
        } catch (NullPointerException thrown) {System.out.println("Удаление курьера не выполнено \nв данном тесте курьер не создавался");}
    }
}
