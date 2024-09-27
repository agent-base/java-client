package ai.agentbase.javaclient;

/**
 * This exception class represents a general exception that may occur while using the AgentBase API client.
 * It is used to handle errors related to the AgentBase API interactions.
 */
public class AgentBaseClientException extends Exception{
    /**
     * Constructs a new AgentBaseClientException with the provided error message.
     *
     * @param message The error message describing the reason for the exception.
     */
    public AgentBaseClientException(String message) {
        super(message);
    }
}
/**
 * This exception class represents an exception that occurs specifically during AgentBase API request operations.
 * It is used to handle errors related to sending requests to the AgentBase API.
 */
class AgentBaseRequestException extends AgentBaseClientException {
    /**
     * Constructs a new AgentBaseRequestException with the provided error message.
     *
     * @param message The error message describing the reason for the request exception.
     */
    public AgentBaseRequestException(String message) {
        super(message);
    }
}