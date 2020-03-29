package sakura.kooi.MoocSearch.utils;


import kong.unirest.Callback;
import kong.unirest.UnirestException;

import javax.net.ssl.SSLHandshakeException;
import java.net.ConnectException;

public abstract class SourceCallback<T> implements Callback<T> {
    private AnswerCallback callback;
    public SourceCallback(AnswerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void failed(UnirestException e) {
        if (e.getCause() instanceof ConnectException) {
            callback.failed("无法连接到服务器");
        } else if (e.getCause() instanceof SSLHandshakeException) {
            callback.failed("SSL证书错误");
        } else {
            e.printStackTrace();
            callback.failed("查询出错");
        }
    }

    @Override
    public void cancelled() {
        callback.failed("查询取消");
    }
}
