package inv;

import inv.dto.Client;
import inv.dto.SuccessResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class ClientAPITest extends BaseAPITest {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientAPITest.class);

  @BeforeEach
    public void beforeEachTest(TestInfo testInfo){
        LOGGER.info("Starting test: " + testInfo.getDisplayName());
    }

        @Test
		 @DisplayName("Can create client") void canCreateClient() {
			Client client =
					new Client("Pragmatic24022" + LocalDate.now(), "Sofia", "Ivan Stranski",
							false, "Alex", "BG112266445566");
			Response createResponse =
					api.clientAPI().createClient(client);
			Assertions.assertEquals(201, createResponse.statusCode());
			//Assertions.assertTrue(createResponse.getBody().asString().contains("Клиента е създаден успешно!")); }
		}
    @Test
    @DisplayName("Can get all clients")
    void canGetAllClients() {
        Response getAllResponse = api.clientAPI().getAll();
        Assertions.assertEquals(200, getAllResponse.statusCode());
    }


    @Test
    @DisplayName("Can delete existing client")
    void canDeleteExistingClient() {
        Response response = api.clientAPI().deleteClient(1574);
        Assertions.assertEquals(200, response.statusCode());
        SuccessResponse successResponse = GSON.fromJson(response.body().asString(), SuccessResponse.class);
        Assertions.assertEquals("Клиента е изтрит", successResponse.getId());
    }

    @Test
    @DisplayName("Can get existing client")
    void canGetExistingClient() {
        Response response = api.clientAPI().getClient(1574);
        Assertions.assertEquals(200, response.statusCode());
    }
}
