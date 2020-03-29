package sakura.kooi.MoocSearch.utils;

public interface AnswerCallback {
    void completed(String answer);
    void failed(String message);
}
