package ai.agentbase.javaclient;

import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * This class contains unit tests for the AgentBaseClient class, focusing on its methods and interactions.
 */
class AgentBaseClientTest {

    private static final String TEST_API_KEY = "testApiKey";
    private static final String TEST_BASE_URL = "http://testBaseUrl";
    private AgentBaseClient agentbaseClient;

    @Mock
    private OkHttpClient mockClient;

    @Mock
    private Call mockCall;

    @Mock
    private Response mockResponse;

    /**
     * Sets up the test environment before each test case.
     *
     * @throws IOException If an I/O error occurs.
     */
    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        agentbaseClient = new AgentBaseClient(TEST_API_KEY, TEST_BASE_URL);
        // Use reflection to inject the mock client into the AgentBaseClient
        // Alternatively, consider adding a constructor to accept the OkHttpClient
        try {
            java.lang.reflect.Field clientField = AgentBaseClient.class.getDeclaredField("client");
            clientField.setAccessible(true);
            clientField.set(agentbaseClient, mockClient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests the getApplicationParameters method of the AgentBaseClient class.
     *
     * @throws Exception If an error occurs during the test.
     */
    @Test
    public void testGetApplicationParameters() throws Exception {
        when(mockResponse.isSuccessful()).thenReturn(true);
        String testUser = "user123";

        agentbaseClient.getApplicationParameters(testUser);

        verify(mockClient).newCall(any(Request.class));
        verify(mockCall).execute();
    }

    /**
     * Tests the messageFeedback method of the AgentBaseClient class.
     *
     * @throws Exception If an error occurs during the test.
     */
    @Test
    public void testMessageFeedback() throws Exception {
        when(mockResponse.isSuccessful()).thenReturn(true);

        agentbaseClient.messageFeedback("12345", "good", "user123");

        verify(mockClient).newCall(any(Request.class));
        verify(mockCall).execute();
    }

}
