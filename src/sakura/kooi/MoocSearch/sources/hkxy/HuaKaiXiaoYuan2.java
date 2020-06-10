package sakura.kooi.MoocSearch.sources.hkxy;

import sakura.kooi.MoocSearch.utils.AnswerCallback;

public class HuaKaiXiaoYuan2 implements Runnable {
    private String question;
    private AnswerCallback callback;

    public HuaKaiXiaoYuan2(String question, AnswerCallback callback) {
        this.question = question;
        this.callback = callback;
    }

    @Override
    public void run() {
        HuaKaiXiaoYuanShared.answer(question, callback, "3");
    }
}
