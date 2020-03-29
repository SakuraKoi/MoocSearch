package sakura.kooi.MoocSearch.sources.XuanXiu365;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import sakura.kooi.MoocSearch.utils.AnswerCallback;

import java.util.concurrent.atomic.AtomicLong;

public class XuanXiu365 implements Runnable {
    private String question;
    private AnswerCallback callback;
    public XuanXiu365(String question, AnswerCallback callback) {
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
        delay.set(System.currentTimeMillis() + 1000);
        HttpResponse<String> httpResponse = Unirest.get("http://tiku.xuanxiu365.com/index.php")
                .header("Referer", "http://tiku.xuanxiu365.com/index.php")
                .queryString("time", System.currentTimeMillis())
                .queryString("q", question)
                .asString();
        if (httpResponse.getStatus() != 200) {
            callback.failed("HTTP错误: "+httpResponse.getStatus());
            return;
        }
        String body = httpResponse.getBody();
        int startIndex = body.indexOf("<b>答案：</b>");
        if (startIndex == -1) {
            callback.failed("未找到答案");
            return;
        }
        int start = startIndex + 10;
        int end = body.indexOf("</div>", start);
        if (startIndex == -1) {
            callback.failed("未找到答案");
            return;
        }
        String answer = body.substring(start, end);
        if (answer.trim().isEmpty()) {
            callback.failed("未找到答案");
            return;
        }
        callback.completed(answer.trim());
    }

}
