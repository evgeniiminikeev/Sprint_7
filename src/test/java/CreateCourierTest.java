import dto.CourierCreateRequest;
import dto.CourierDeleteRequest;
import dto.CourierLoginRequest;
import generator.CourierGenerator;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import static config.Config.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class CreateCourierTest {
    private CourierGenerator generator = new CourierGenerator();
    String login = generator.login();
    String password = generator.password();
    String firstName = generator.firstName();

    @Before
    public void setUp() {

    }

    @Step("Creating a courier")
    public Response createCourier(String login, String password, String firstName) {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);
        return given().baseUri(BASE_URI).contentType(ContentType.JSON).body(courierCreateRequest).when().post(COURIER_CREATE_API);
    }
    @Step("Checking the result of successful courier creation")
    public void checkSuccessfulCreation(Response response) {
        response.then().statusCode(201).assertThat().body("ok",equalTo(true));
    }
    @Step("Checking the result of failed courier creation")
    public void checkFailedCreation(Response response, int status, String errorMessage) {
        response.then().statusCode(status).assertThat().body("message",equalTo(errorMessage));
    }
    @Step("Get courier's id")
    public String getCourierId(String login, String password) {
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest(login, password);
        return given().baseUri(BASE_URI).contentType(ContentType.JSON).body(courierLoginRequest).when().post(COURIER_LOGIN_API).then().extract().path("id").toString();
    }
    @Step("Delete created courier")
    public void deleteCourier(String courierId) {
        CourierDeleteRequest courierDeleteRequest = new CourierDeleteRequest(courierId);
        given().baseUri(BASE_URI).contentType(ContentType.JSON).body(courierDeleteRequest).when().delete(COURIER_DELETE_API + courierId);
    }

    @Test
    @DisplayName("Creating a courier with all fields")
    @Description("A courier can be created by passing all the fields. A successful request returns \"ok: true\"")
    public void createCourierShouldReturnOk() {
        Response response = createCourier(login,password,firstName);
        checkSuccessfulCreation(response);
    }

    @Test
    @DisplayName("Creating a courier with an existing login")
    @Description("Creating a courier with an existing login results in a 409 error")
    public void createSameCourierShouldReturnFalse() {
        createCourier(login,password,firstName);
        Response response = createCourier(login,password,firstName);
        checkFailedCreation(response, 409, "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @DisplayName("Creating a courier without a firstName")
    @Description("You can create a courier by providing only your login and password. A successful request returns \"ok: true\"")
    public void createCourierWithoutNameShouldReturnOk() {
        Response response = createCourier(login,password,null);
        checkSuccessfulCreation(response);
    }

    @Test
    @DisplayName("Creating a courier without a login")
    @Description("Creating a courier without a login results in a 400 error")
    public void createCourierWithoutLoginShouldReturnError() {
        Response response = createCourier(null,password,firstName);
        checkFailedCreation(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Creating a courier without a password")
    @Description("Creating a courier without a login results in a 400 error")
    public void createCourierWithoutPasswordShouldReturnError() {
        Response response = createCourier(login,null,firstName);
        checkFailedCreation(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @After
    public void tearDown(){
        try {
            String id = getCourierId(login,password);
            deleteCourier(id);
        } catch (NullPointerException thrown) {System.out.println("Удаление курьера не выполнено \nв данном тесте курьер не создавался");}
    }
}
