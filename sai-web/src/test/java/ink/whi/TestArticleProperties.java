package ink.whi;

import org.junit.Test;

/**
 * @author: qing
 * @Date: 2023/10/4
 */
public class TestArticleProperties {
    public Boolean REVIEW = true;

    public synchronized void enable(Boolean enable) {
        REVIEW = enable;
    }

    public synchronized Boolean getReview() {
        return REVIEW;
    }

    @Test
    public void Test() {
        TestArticleProperties article = new TestArticleProperties();
        for (int i = 0; i < 100; i++) {
            new Thread( () -> {
                article.enable(true);
                System.out.println("true ==>" + article.getReview());
            }).start();
            new Thread( () -> {
                article.enable(false);
                System.out.println("false ==>" + article.getReview());
            }).start();
        }
        while (Thread.activeCount() > 2) {
            System.out.println(Thread.activeCount());
            Thread.yield();
        }
        System.out.println(article.getReview());
    }
}
