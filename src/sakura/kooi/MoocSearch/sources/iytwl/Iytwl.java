package sakura.kooi.MoocSearch.sources.iytwl;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import sakura.kooi.MoocSearch.utils.AnswerCallback;
import sakura.kooi.MoocSearch.utils.RateLimiter;

public class Iytwl implements Runnable {
    private String question;
    private AnswerCallback callback;
    public Iytwl(String question, AnswerCallback callback) {
        this.question = question;
        this.callback = callback;
    }

    private static RateLimiter delay = new RateLimiter();

    @Override
    public void run() {
        delay.limit(500);

        HttpResponse<JsonNode> httpResponse = Unirest.get("https://api.iytwl.cn/api/wangke/")
                .queryString("question", question)
                .asJson();
        if (httpResponse.getStatus() != 200) {
            callback.failed("HTTP错误: "+httpResponse.getStatus());
            return;
        }
        JSONObject body = httpResponse.getBody().getObject();
        if (!body.getString("code").equals("success")) {
            callback.failed("未找到答案");
            return;
        }
        String answer = body.getJSONArray("data").getJSONObject(0).getString("answer");
        if (answer.trim().isEmpty()) {
            callback.failed("未找到答案");
            return;
        }
        callback.completed(answer.trim().replace("\u0001", "\n"));
    }
}
