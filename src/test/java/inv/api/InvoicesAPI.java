package inv.api;

import com.jayway.jsonpath.JsonPath;
import inv.dto.Invoices;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class InvoicesAPI extends HTTPClient {

    private static final String INVOICE_URL = "/invoices";
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoicesAPI.class);

    public InvoicesAPI(String token){
        super(token);
    }

    /**
     * Creates new client
     * @param invc Invoices
     * @return response
     */
    public Response createInvoice(Invoices invc){
        return post(INVOICE_URL, GSON.toJson(invc));
    }

    /**
     * Returns a single client
     * @param id invoice id
     * @return response
     */
    public Response getInvoice(int id){
        return get(INVOICE_URL + "/" + id);
    }

    /**
     * Returns a single client
     * @param id invoice id
     * @return response
     */
    public ResponseBody getInvoiceAndValidate(int id){
        return getAndValidateBody(INVOICE_URL + "/" + id);
    }

    /**
     * Returns all invoices from the system
     * @return array of invoices
     */
    public Response getAllInvoices(){
        return get(INVOICE_URL);
    }

    /**
     * Deletes invoice
     * @param id invoice id
     * @return response
     */
    public Response deleteInvoice(int id){
        return delete(INVOICE_URL + "/" + id);
    }

    /**
     * Updates invoice
     * @param id invoice id
     * @param inv invoices body
     * @return response
     */
    public Response updateInvoice(int id, Invoices inv){
        return patch(INVOICE_URL + "/" + id, GSON.toJson(inv));
    }

    public void deleteAll(){
        String responseBody = getAllInvoices().body().asString();
        List<Integer> ids = JsonPath.read(responseBody, "$.invoices[*].id");
        LOGGER.debug("Ids for deletion found:" +  ids.toString());
        ids.forEach(id -> deleteInvoice(id));
    }
}
