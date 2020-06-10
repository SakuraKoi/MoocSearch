package sakura.kooi.MoocSearch.sources.wk92e;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import sakura.kooi.MoocSearch.utils.AnswerCallback;
import sakura.kooi.MoocSearch.utils.RateLimiter;

public class Moecoo implements Runnable {
    private String question;
    private AnswerCallback callback;
    public Moecoo(String question, AnswerCallback callback) {
        this.question = question;
        this.callback = callback;
    }

    private static RateLimiter delay = new RateLimiter();

    @Override
    public void run() {
        delay.limit(500);

        HttpResponse<JsonNode> httpResponse = Unirest.get("http://api.902000.xyz:88/wkapi.php")
                .queryString("q", question)
                .socketTimeout(3000)
                .asJson();
        if (httpResponse.getStatus() != 200) {
            callback.failed("HTTP错误: "+httpResponse.getStatus());
            return;
        }
        JSONObject body = httpResponse.getBody().getObject();
        String answer = body.getString("answer");
        if (answer.equals("查无此题") || answer.trim().isEmpty()) {
            callback.failed("未找到答案");
            return;
        }
        callback.completed(answer.trim().replace("\u0001", "\n"));
    }
}
