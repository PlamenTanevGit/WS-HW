import com.microsoft.playwright.*;
import inv.BaseAPITest;
import inv.api.API;
import inv.api.TokenAPI;
import inv.dto.Client;
import inv.dto.Item;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

public class LoginPageTest extends BaseAPITest {
    private static Browser browser = null;
    private Page page;
    private final String EMAIL_SELECTOR = "#loginusername";
    private final String PASSWORD_SELECTOR = "#loginpassword";
    private final String LOGIN_BTN_SELECTOR = "input.selenium-submit-button";
    private final String COMPANY_NAME_SELECTOR = "//div[@id='wellcome']/h2";
    private final String USER_PANEL_SELECTOR = "div.userpanel-header";
    private final String LOGOUT_LINK_SELECTOR = "a.selenium-button-logout";
    private final String LOGOUT_SUCCESS_MSG_SELECTOR = "#okmsg";

    @BeforeAll
    static void beforeAll() {
        browser = Playwright.create()
                .chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(false));
    }


    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        System.out.println("Starting test:" + testInfo.getDisplayName());
        //Creates new page instance
        page = browser.newPage();
        //Navigates to (Login page) in this case
        page.navigate("https://ws1.inv.bg/login");
    }

    @AfterEach
    void afterEach() {
        page.close();
    }

    @AfterAll
    static void afterAll() {
        browser.close();
    }

    @Test
    @Tag("smoke")
    @DisplayName("Can login successfully with valid credentials")
    void canLoginSuccessfullyWithValidCredentials() {
        login();
    }

    @Test
    @Tag("smoke")
    @DisplayName("Can login successfully with valid credentials and logout")
    void canLoginSuccessfullyWithValidCredentialsAndLogout() {
        login();
        //Logout
        page.click(USER_PANEL_SELECTOR);
        page.click(LOGOUT_LINK_SELECTOR);
        //Logout message check
        String logoutMsg = page.textContent(LOGOUT_SUCCESS_MSG_SELECTOR).replace("\u00a0",""); //TODO: report as a bug :)
        Assertions.assertEquals("Вие излязохте от акаунта си.", logoutMsg);
    }

    @ParameterizedTest
    @ValueSource(strings = {"име на артикул", "item_name", "search_test"})
    @Tag("item")
    @Tag("positive")
    void canSearchForExistingItems(String name){
        //Clean all existing items
        api.itemAPI().deleteAll();
        //Create new item to search for
        Item item = new Item(name, 20.00, "кг.", 10.0, "EUR");
        Response resp = api.itemAPI().createItem(item);
        Assertions.assertEquals(201, resp.statusCode());
        login();
        //Navigates to Item page
        page.click("#tabs_objects");
        //Expand search form
        page.click("#searchbtn");
        //Enter search criteria
        page.fill("input[name='nm']", name);
        //Trigger search
        page.click("input[name='s']");
        //Check the search result output
        String searchResult = page.textContent("a.faktura_id");
        Assertions.assertTrue(searchResult.contains(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test", "Test15", "Test16"})
    @Tag("client")
    @Tag("positive")
    void canSearchForExistingClients(String name){
        //Clean all existing clients
       // api.clientAPI().deleteAll();
        //Create new client to search for
        Client client = new Client(name + LocalDate.now(), "Sofia", "Ivan Stranski",
                false, "Alex", "BG112288445566");
        Response createResponse = api.clientAPI().createClient(client);
        Assertions.assertEquals(201, createResponse.statusCode());
        try {
            Thread.sleep(5330);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        login();
        //Navigates to client page
        page.click("#tabs_clients");
        //Expand search form
        page.click("#searchbtn");
        //Enter search criteria
        page.fill("input[name='fnm']", name);
        //Trigger search
        page.click("input[name='s']");
        //Check the search result output
        String searchResult = page.textContent("a.faktura_id");
        Assertions.assertTrue(searchResult.contains(name));
    }

    private void login() {
        String companyName = page.textContent(COMPANY_NAME_SELECTOR);
        //Assertions.assertEquals("QA Ground", companyName);
        //Enter email
        page.fill(EMAIL_SELECTOR, "ws1@abv.bg");
        //Enter password
        page.fill(PASSWORD_SELECTOR, "ws123");
        //Click Login button
        page.waitForNavigation(() -> {
            page.click(LOGIN_BTN_SELECTOR);
        });
        String loggedUser = page.textContent(USER_PANEL_SELECTOR);
        Assertions.assertEquals("ws1@abv.bg", loggedUser);
    }
}
