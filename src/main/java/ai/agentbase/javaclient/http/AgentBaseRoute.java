package ai.agentbase.javaclient.http;

/**
 * @author Ziyao_Zhu
 */
public class AgentBaseRoute {
    public String method;
    public  String url;

    public AgentBaseRoute(String method, String url) {
        this.method = method;
        this.url = url;
    }
}