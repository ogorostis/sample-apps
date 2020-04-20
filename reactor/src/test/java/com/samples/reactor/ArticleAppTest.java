package com.samples.reactor;

import com.examples.common.SampleException;
import org.testng.annotations.Test;

public class ArticleAppTest {
    @Test
    public void testFrArticleApp() {
        final ArticleApp app = new ArticleApp();
        app.analyzeArticle("fr");
    }

    @Test
    public void testItArticleApp() {
        final ArticleApp app = new ArticleApp();
        app.analyzeArticle("it");
    }

    @Test(expectedExceptions = SampleException.class)
    public void testUkArticleApp() {
        final ArticleApp app = new ArticleApp();
        app.analyzeArticle("uk");
    }
}
