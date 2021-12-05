package inv;

import inv.dto.Invoices;
import inv.dto.Item;
import inv.dto.SuccessResponse;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;

public class InvoicTest extends BaseAPITest {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientAPITest.class);

    @BeforeEach
    public void beforeEachTest(TestInfo testInfo){
        LOGGER.info("Starting test: " + testInfo.getDisplayName());
    }
    @Test
    @DisplayName("Can get all Invoices")
    void canGetAllInvoices() {
        Response getAllResponse = api.invoicesAPI().getAllInvoices();
        Assertions.assertEquals(200, getAllResponse.statusCode());
    }

    @Test
    @DisplayName("Can create Invoice")
    void canCreateInvoice() {
        String[] paymentMethod ={"paypal"};
        Item item = new Item("Крушки", 20.00, "бр.", 10.0, "EUR",0,29.5,177,5);
        Item[] Items ={item};
        Invoices invoiceItems=new Invoices("dan","Dona Joe","burgeria","Coona","ул. Климент Охридски № 125",
                1230506989,true,"BG1234560800","Петър Страшимиров",false,821019421,"Иван Георгиев","BGN","bank",
                paymentMethod,"paid",35,35,7,0,42, Items);
        Response createResponse = api.invoicesAPI().createInvoice(invoiceItems);
        Assertions.assertEquals(201, createResponse.statusCode());
        Assertions.assertTrue(createResponse.getBody().asString().contains("id"));
    }

   @Test
   @DisplayName("Can update existing Invoice")
   void canUpdateExistingInvoice() {
       String[] paymentMethod ={"paypal"};
       Item item = new Item("Крушки", 20.00, "бр.", 10.0, "EUR",0,29.5,177,5);
       Item[] Items ={item};
       Invoices invoiceItems=new Invoices("dan","Възраждане АД","България","София","ул. Климент Охридски № 125",
               1230506989,true,"BG1234560800","Петър Страшимиров",false,821019421,"Иван Георгиев","BGN","bank",
               paymentMethod,"Unpaid",35,35,7,0,425, Items);
       Response createResponse = api.invoicesAPI().createInvoice(invoiceItems);
       Assertions.assertEquals(201, createResponse.statusCode());
       Assertions.assertTrue(createResponse.getBody().asString().contains("id"));
       //Deserialize the success response into success response object
       SuccessResponse successResponse = GSON.fromJson(createResponse.body().asString(), SuccessResponse.class);
       //Change the Invoice Status in the dto
       invoiceItems.setStatus("paid");
       //Update the Invoice
       Response updateResponse = api.invoicesAPI().updateInvoice(Integer.valueOf(successResponse.getId()), invoiceItems);
       Assertions.assertEquals(204, updateResponse.statusCode());
   }

    @Test
    @DisplayName("Can delete existing Invoice")
    void canDeleteExistingInvoice() {
        Response response = api.invoicesAPI().deleteInvoice(33);
        Assertions.assertEquals(204, response.statusCode());
        SuccessResponse successResponse = GSON.fromJson(response.body().asString(), SuccessResponse.class);
    }

  @Test
  @DisplayName("Can get existing Invoice")
  void canGetExistingInvoices() {
      Response response = api.invoicesAPI().getInvoice(30);
      Assertions.assertEquals(200, response.statusCode());

  }
    @Test
    @DisplayName("Get existing Invoice and validate body")
    void getExistingInvoicesAndValidateBody() {
        ResponseBody body = api.invoicesAPI().getInvoiceAndValidate(18);
        // Get Response Body as String
        String bodyStringValue = body.asString();

        Assertions.assertTrue(bodyStringValue.contains("from_name"));

        JsonPath jsonPathEvaluator = body.jsonPath();
        ArrayList<String> items = jsonPathEvaluator.get("items");
        Assertions.assertTrue(items.size()>0);

    }
}
