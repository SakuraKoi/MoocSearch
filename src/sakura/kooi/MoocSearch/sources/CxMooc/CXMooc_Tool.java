package sakura.kooi.MoocSearch.sources.CxMooc;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import sakura.kooi.MoocSearch.utils.AnswerCallback;

import java.util.concurrent.atomic.AtomicLong;

public class CXMooc_Tool implements Runnable {
    private String question;
    private AnswerCallback callback;
    public CXMooc_Tool(String question, AnswerCallback callback) {
        this.question = question;
        this.callback = callback;
    }

    private static AtomicLong delay = new AtomicLong(-1L);

    @Override
    public void run() {
        while (System.currentTimeMillis() < delay.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) { }
        }
        delay.set(System.currentTimeMillis() + 2000);
        HttpResponse<JsonNode> httpResponse = Unirest.post("https://cx.icodef.com/v2/answer?platform=cx")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("topic[0]", question)
                .asJson();
        if (httpResponse.getStatus() != 200) {
            callback.failed("HTTP错误: "+httpResponse.getStatus());
            return;
        }

        JSONArray array = httpResponse.getBody().getArray();
        final JSONObject root = array.getJSONObject(0);
        final JSONArray result = root.getJSONArray("result");
        StringBuilder answer = new StringBuilder();

        if (result.length() == 1) {
            result.getJSONObject(0).getJSONArray("correct").forEach(obj -> {
                answer.append("\n");
                final JSONObject correct = (JSONObject) obj;
                answer.append(correct.get("content"));
            });
        } else {
            result.forEach(obj -> {
                answer.append("\n");
                final JSONObject ques = (JSONObject) obj;
                answer.append(ques.getString("topic"));
                answer.append(": ");
                final StringBuilder sb = new StringBuilder();
                ques.getJSONArray("correct").forEach(corr -> {
                    final JSONObject correct = (JSONObject) corr;
                    sb.append(correct.get("content"));
                    sb.append(" | ");
                });
                sb.delete(sb.length() - 3, sb.length() - 1);
                answer.append(sb.toString());
            });
        }
        if (answer.length() > 0) {
            answer.deleteCharAt(0);
            callback.completed(answer.toString());
        } else {
            callback.failed("未找到答案");
        }
    }
}
