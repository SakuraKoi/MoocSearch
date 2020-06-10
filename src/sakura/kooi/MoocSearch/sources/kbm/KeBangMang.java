package sakura.kooi.MoocSearch.sources.kbm;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import sakura.kooi.MoocSearch.utils.AnswerCallback;
import sakura.kooi.MoocSearch.utils.AnswerUtils;
import sakura.kooi.MoocSearch.utils.RateLimiter;

public class KeBangMang implements Runnable {
    private String question;
    private AnswerCallback callback;
    public KeBangMang(String question, AnswerCallback callback) {
        this.question = question;
        this.callback = callback;
    }

    private static String cookie = null;
    private static RateLimiter delay = new RateLimiter();

    @Override
    public void run() {
        delay.limit(1000);

        HttpResponse<JsonNode> httpResponse = Unirest.post("https://www.150s.cn/topic/getAnswer")
                .header(":authority", "www.150s.cn")
                .header(":method", "POST")
                .header(":path", "/topic/getAnswer")
                .header(":scheme", "https")
                .header("accept", "application/json, text/javascript, */*; q=0.01")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "en,zh-CN;q=0.9,zh;q=0.8,ja;q=0.7,und;q=0.6")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("origin", "https://www.150s.cn")
                .header("referer", "https://www.150s.cn/")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36")
                .header("x-requested-with", "XMLHttpRequest")
                .header("cookie", "JSESSIONID="+cookie)
                .field("title", question)
                .asJson();
        if (httpResponse.getStatus() != 200) {
            callback.failed("HTTP错误: "+httpResponse.getStatus());
            return;
        }
        JSONObject body = httpResponse.getBody().getObject();

        String answer = body.getString("answer");
        if (answer.contains("该题目还未收录") || answer.trim().isEmpty()) {
            callback.failed("未找到答案");
            return;
        }
        callback.completed(answer.trim().replace("\u0001", "\n"));
    }

    static {
        HttpResponse<String> response = Unirest.get("https://www.150s.cn/").header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36")
                .asString();
        if (response.getStatus() != 200) {
            AnswerUtils.getInitializerLogger().error("题库三 获取Session失败, 使用内置Session");
            cookie = "F372F15AB2B496D6A511B7156B9242B7";
        } else {
            String setCookie = response.getHeaders().getFirst("set-cookie");
            int start = setCookie.indexOf("JSESSIONID=") + "JSESSIONID=".length();
            cookie = setCookie.substring(start, setCookie.indexOf(';'));
            AnswerUtils.getInitializerLogger().info("题库三 获取Session成功 {}", cookie);
        }
    }
}
