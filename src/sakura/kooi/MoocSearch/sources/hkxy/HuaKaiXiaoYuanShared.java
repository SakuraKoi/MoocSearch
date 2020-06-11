package sakura.kooi.MoocSearch.sources.hkxy;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import sakura.kooi.MoocSearch.utils.AnswerCallback;
import sakura.kooi.MoocSearch.utils.AnswerUtils;
import sakura.kooi.MoocSearch.utils.RateLimiter;

public class HuaKaiXiaoYuanShared {
    private static RateLimiter delay = new RateLimiter();
    public static String token;

    static {
        refreshToken();
    }

    public static void answer(String question, AnswerCallback callback, String type) {
        if (token == null) {
            refreshToken();
            if (token == null) {
                callback.failed("获取Token失败");
                return;
            }
        }
        delay.limit(500);

        HttpResponse<JsonNode> httpResponse = Unirest.post("https://jk.fm210.cn/web.php")
                .header("host", "jk.fm210.cn")
                .header("accept", "*/*")
                .header("x-requested-with", "XMLHttpRequest")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("origin", "https://jk.fm210.cn")
                .header("sec-fetch-site", "same-origin")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-dest", "empty")
                .header("referer", "https://jk.fm210.cn/")
                .header("accept-encoding", "gzip")
                .header("accept-language", "en,zh-CN;q=0.9,zh;q=0.8,ja;q=0.7,und;q=0.6")
                .field("token", encrypt(question, HuaKaiXiaoYuanShared.token))
                .field("type", type)
                .field("question", question)
                .asJson();
        if (httpResponse.getStatus() != 200) {
            callback.failed("HTTP错误: "+httpResponse.getStatus());
            return;
        }
        JSONObject body = httpResponse.getBody().getObject();
        if (!body.isNull("tm") && body.getString("tm").contains("token错误")) {
            refreshToken();
            callback.failed("Token过期, 请重试");
            return;
        }
        String answer = body.getString("da");
        if ("\n".equals(answer) || answer.contains("还未收录") || answer.trim().isEmpty()) {
            callback.failed("未找到答案");
            return;
        }
        callback.completed(answer.trim().replace("\u0001", "\n"));
    }

    private static String encrypt(String question, String token) {
        return DigestUtils.md5Hex(question + token + "fm");
    }

    private static void refreshToken() {
        HttpResponse<String> response = Unirest.get("https://jk.fm210.cn/").header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36")
                .asString();
        if (response.getStatus() != 200) {
            AnswerUtils.getInitializerLogger().error("题库四/五/六 获取Token失败");
            token = null;
        } else {
            String document = response.getBody();
            int start = document.indexOf("window.jjm(question,\"") + "window.jjm(question,\"".length();
            int end = document.indexOf("\");", start);
            token = document.substring(start, end);
            AnswerUtils.getInitializerLogger().info("题库四/五/六 获取Token成功 {}", token);
        }
    }
}
