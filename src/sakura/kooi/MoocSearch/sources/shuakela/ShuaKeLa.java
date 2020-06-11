package sakura.kooi.MoocSearch.sources.shuakela;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import sakura.kooi.MoocSearch.utils.AnswerCallback;
import sakura.kooi.MoocSearch.utils.RateLimiter;

public class ShuaKeLa implements Runnable {
    private String question;
    private AnswerCallback callback;
    public ShuaKeLa(String question, AnswerCallback callback) {
        this.question = question;
        this.callback = callback;
    }

    private static RateLimiter delay = new RateLimiter();


    @Override
    public void run() {
        delay.limit(500);

        HttpResponse<JsonNode> httpResponse = Unirest.get("http://ct.shuakela.top/wkapi.php")
                .header("accept", "application/json, text/javascript, */*; q=0.01")
                .header("accept-encoding", "gzip, deflate")
                .header("accept-language", "en,zh-CN;q=0.9,zh;q=0.8,ja;q=0.7,und;q=0.6")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("host", "ct.shuakela.top")
                .header("referer", "http://ct.shuakela.top/")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36")
                .header("x-requested-with", "XMLHttpRequest")
                .queryString("tm", question)
                .asJson();
        if (httpResponse.getStatus() != 200) {
            callback.failed("HTTP错误: "+httpResponse.getStatus());
            return;
        }
        JSONObject body = httpResponse.getBody().getObject();

        String answer = body.getString("answer");
        if (answer.contains("抱歉找不到结果") || answer.trim().isEmpty()) {
            callback.failed("未找到答案");
            return;
        }
        callback.completed(answer.trim().replace(" --- ", "\n"));
    }
}
