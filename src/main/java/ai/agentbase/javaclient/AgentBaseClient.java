package ai.agentbase.javaclient;

import ai.agentbase.javaclient.constants.AgentBaseServerConstants;
import ai.agentbase.javaclient.http.AgentBaseRoute;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;

import java.io.IOException;
/**
 * This class serves as a client for interacting with the AgentBase API.
 * It provides methods for sending various types of requests to the API.
 */
public class AgentBaseClient {

    // Constants representing different API routes
    public static final AgentBaseRoute APPLICATION = new AgentBaseRoute("GET", "/parameters?user=%s");
    public static final AgentBaseRoute FEEDBACK = new AgentBaseRoute("POST", "/messages/%s/feedbacks");
    public static final AgentBaseRoute CREATE_COMPLETION_MESSAGE = new AgentBaseRoute("POST", "/completion-messages");
    public static final AgentBaseRoute CREATE_CHAT_MESSAGE = new AgentBaseRoute("POST", "/chat-messages");
    public static final AgentBaseRoute GET_CONVERSATION_MESSAGES = new AgentBaseRoute("GET", "/messages?%s");
    public static final AgentBaseRoute GET_CONVERSATIONS = new AgentBaseRoute("GET", "/conversations");
    public static final AgentBaseRoute RENAME_CONVERSATION = new AgentBaseRoute("PATCH", "/conversations/%s");
    public static final AgentBaseRoute DELETE_CONVERSATION = new AgentBaseRoute("DELETE", "/conversations/%s");

    private String apiKey;
    private final String baseUrl;
    private final OkHttpClient client;

    /**
     * Constructs a new AgentBaseClient with the provided API key and default base URL.
     *
     * @param apiKey The API key to use for authentication.
     */
    public AgentBaseClient(String apiKey) {
        this(apiKey, AgentBaseServerConstants.BASE_URL);
    }

    /**
     * Constructs a new AgentBaseClient with the provided API key and base URL.
     *
     * @param apiKey   The API key to use for authentication.
     * @param baseUrl  The base URL of the AgentBase API.
     */
    public AgentBaseClient(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.client = new OkHttpClient();
    }

    /**
     * Updates the API key used for authentication.
     *
     * @param apiKey The new API key.
     */
    public void updateApiKey(String apiKey) {
        this.apiKey = apiKey;
    }


    /**
     * Sends an HTTP request to the AgentBase API.
     *
     * @param route      The API route to send the request to.
     * @param formatArgs Format arguments for route URL placeholders.
     * @param body       The request body, if applicable.
     * @return The HTTP response containing the result of the API request.
     * @throws AgentBaseClientException If an error occurs while sending the request.
     */
    public Response sendRequest(AgentBaseRoute route, String[] formatArgs, RequestBody body) throws AgentBaseClientException {
        try {
            String formattedURL = (formatArgs != null && formatArgs.length > 0)
                    ? String.format(route.url, (Object[]) formatArgs)
                    : route.url;

            Request request = new Request.Builder()
                    .url(baseUrl + formattedURL)
                    .method(route.method, body)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new AgentBaseRequestException("Request failed with status: " + response.code());
            }
            return response;
        } catch (IOException e) {
            throw new AgentBaseClientException("Error occurred while sending request: " + e.getMessage());
        }
    }


    /**
     * Sends a message feedback to the AgentBase API.
     *
     * @param messageId The ID of the message to provide feedback for.
     * @param rating    The feedback rating.
     * @param user      The user providing the feedback.
     * @return The HTTP response containing the result of the API request.
     * @throws AgentBaseClientException If an error occurs while sending the request.
     */
    public Response messageFeedback(String messageId, String rating, String user) throws AgentBaseClientException {
        JSONObject json = new JSONObject();
        json.put("rating", rating);
        json.put("user", user);

        return sendRequest(FEEDBACK, new String[]{messageId}, createJsonPayload(json));
    }

    /**
     * Retrieves application parameters from the AgentBase API.
     *
     * @param user The user for whom the application parameters are retrieved.
     * @return The HTTP response containing the result of the API request.
     * @throws AgentBaseClientException If an error occurs while sending the request.
     */
    public Response getApplicationParameters(String user) throws AgentBaseClientException {
        return sendRequest(APPLICATION, new String[]{user}, null);
    }

    /**
     * Creates a request body with the given JSON object.
     *
     * @param jsonObject The JSON object to be used in the request body.
     * @return The created request body.
     */
    RequestBody createJsonPayload(JSONObject jsonObject) {
        return RequestBody.create(jsonObject.toJSONString(), MediaType.parse("application/json"));
    }
}
