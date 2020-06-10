package sakura.kooi.MoocSearch.sources.hkxy;

import sakura.kooi.MoocSearch.utils.AnswerCallback;

public class HuaKaiXiaoYuan1 implements Runnable {
    private String question;
    private AnswerCallback callback;

    public HuaKaiXiaoYuan1(String question, AnswerCallback callback) {
        this.question = question;
        this.callback = callback;
    }

    @Override
    public void run() {
        HuaKaiXiaoYuanShared.answer(question, callback, "2");
    }
}
