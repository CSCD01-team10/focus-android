package org.mozilla.focus.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mozilla.focus.browser.LocalizedContent;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class UrlUtilsTest {
    @Test
    public void isValidSearchQueryUrl() {
        assertTrue(UrlUtils.isValidSearchQueryUrl("https://example.com/search/?q=%s"));
        assertTrue(UrlUtils.isValidSearchQueryUrl("http://example.com/search/?q=%s"));
        assertTrue(UrlUtils.isValidSearchQueryUrl("http-test-site.com/search/?q=%s"));
        assertFalse(UrlUtils.isValidSearchQueryUrl("httpss://example.com/search/?q=%s"));

        assertTrue(UrlUtils.isValidSearchQueryUrl("example.com/search/?q=%s"));
        assertTrue(UrlUtils.isValidSearchQueryUrl(" example.com/search/?q=%s "));

        assertFalse(UrlUtils.isValidSearchQueryUrl("htps://example.com/search/?q=%s"));
    }

    @Test
    public void urlsMatchExceptForTrailingSlash() {
        assertTrue(UrlUtils.urlsMatchExceptForTrailingSlash("http://www.mozilla.org", "http://www.mozilla.org"));
        assertTrue(UrlUtils.urlsMatchExceptForTrailingSlash("http://www.mozilla.org/", "http://www.mozilla.org"));
        assertTrue(UrlUtils.urlsMatchExceptForTrailingSlash("http://www.mozilla.org", "http://www.mozilla.org/"));

        assertFalse(UrlUtils.urlsMatchExceptForTrailingSlash("http://mozilla.org", "http://www.mozilla.org"));
        assertFalse(UrlUtils.urlsMatchExceptForTrailingSlash("http://www.mozilla.org/", "http://mozilla.org"));

        assertFalse(UrlUtils.urlsMatchExceptForTrailingSlash("http://www.mozilla.org", "https://www.mozilla.org"));
        assertFalse(UrlUtils.urlsMatchExceptForTrailingSlash("https://www.mozilla.org", "http://www.mozilla.org"));

        // Same length of domain, but otherwise different:
        assertFalse(UrlUtils.urlsMatchExceptForTrailingSlash("http://www.allizom.org", "http://www.mozilla.org"));
        assertFalse(UrlUtils.urlsMatchExceptForTrailingSlash("http://www.allizom.org/", "http://www.mozilla.org"));
        assertFalse(UrlUtils.urlsMatchExceptForTrailingSlash("http://www.allizom.org", "http://www.mozilla.org/"));

        // Check upper/lower case is OK:
        assertTrue(UrlUtils.urlsMatchExceptForTrailingSlash("http://www.MOZILLA.org", "http://www.mozilla.org"));
        assertTrue(UrlUtils.urlsMatchExceptForTrailingSlash("http://www.MOZILLA.org/", "http://www.mozilla.org"));
        assertTrue(UrlUtils.urlsMatchExceptForTrailingSlash("http://www.MOZILLA.org", "http://www.mozilla.org/"));
    }

    @Test
    public void isPermittedResourceProtocol() {
        assertFalse(UrlUtils.isPermittedResourceProtocol(""));
        assertFalse(UrlUtils.isPermittedResourceProtocol(null));

        assertTrue(UrlUtils.isPermittedResourceProtocol("http"));
        assertTrue(UrlUtils.isPermittedResourceProtocol("https"));

        assertTrue(UrlUtils.isPermittedResourceProtocol("data"));
        assertTrue(UrlUtils.isPermittedResourceProtocol("file"));

        assertFalse(UrlUtils.isPermittedResourceProtocol("nielsenwebid"));
    }

    @Test
    public void isPermittedProtocol() {
        assertFalse(UrlUtils.isSupportedProtocol(""));
        assertFalse(UrlUtils.isSupportedProtocol(null));

        assertTrue(UrlUtils.isSupportedProtocol("http"));
        assertTrue(UrlUtils.isSupportedProtocol("https"));
        assertTrue(UrlUtils.isSupportedProtocol("error"));
        assertTrue(UrlUtils.isSupportedProtocol("data"));

        assertFalse(UrlUtils.isSupportedProtocol("market"));
    }

    @Test
    public void testIsUrl() {
        assertTrue(UrlUtils.isUrl("http://www.mozilla.org"));
        assertTrue(UrlUtils.isUrl("https://www.mozilla.org"));
        assertTrue(UrlUtils.isUrl("https://www.mozilla.org "));
        assertTrue(UrlUtils.isUrl(" https://www.mozilla.org"));
        assertTrue(UrlUtils.isUrl(" https://www.mozilla.org "));
        assertTrue(UrlUtils.isUrl("https://www.mozilla.org/en-US/internet-health/"));
        assertTrue(UrlUtils.isUrl("file:///mnt/sdcard/"));
        assertTrue(UrlUtils.isUrl("mozilla.org"));

        assertFalse(UrlUtils.isUrl("Hello World"));
        assertFalse(UrlUtils.isUrl("Mozilla"));
    }

    @Test
    public void testNormalize() {
        assertEquals("http://www.mozilla.org", UrlUtils.normalize("http://www.mozilla.org"));
        assertEquals("https://www.mozilla.org", UrlUtils.normalize("https://www.mozilla.org"));
        assertEquals("https://www.mozilla.org/en-US/internet-health/", UrlUtils.normalize("https://www.mozilla.org/en-US/internet-health/"));
        assertEquals("file:///mnt/sdcard/", UrlUtils.normalize("file:///mnt/sdcard/"));

        assertEquals("http://mozilla.org", UrlUtils.normalize("mozilla.org"));
        assertEquals("http://mozilla.org", UrlUtils.normalize("http://mozilla.org "));
        assertEquals("http://mozilla.org", UrlUtils.normalize(" http://mozilla.org "));
        assertEquals("http://mozilla.org", UrlUtils.normalize(" http://mozilla.org"));
        assertEquals("http://localhost", UrlUtils.normalize("localhost"));
    }

    @Test
    @SuppressLint("AuthLeak")
    public void testStripUserInfo() {
        assertEquals("", UrlUtils.stripUserInfo(null));
        assertEquals("", UrlUtils.stripUserInfo(""));

        assertEquals("https://www.mozilla.org", UrlUtils.stripUserInfo("https://user:password@www.mozilla.org"));
        assertEquals("https://www.mozilla.org", UrlUtils.stripUserInfo("https://user@www.mozilla.org"));

        assertEquals("user@mozilla.org", UrlUtils.stripUserInfo("user@mozilla.org"));

        assertEquals("ftp://mozilla.org", UrlUtils.stripUserInfo("ftp://user:password@mozilla.org"));

        assertEquals("öäü102ß", UrlUtils.stripUserInfo("öäü102ß"));
    }

    @Test
    public void isInternalErrorURL() {
        assertTrue(UrlUtils.isInternalErrorURL("data:text/html;charset=utf-8;base64,"));

        assertFalse(UrlUtils.isInternalErrorURL("http://www.mozilla.org"));
        assertFalse(UrlUtils.isInternalErrorURL("https://www.mozilla.org/en-us/about"));
        assertFalse(UrlUtils.isInternalErrorURL("www.mozilla.org"));
        assertFalse(UrlUtils.isInternalErrorURL("error:-8"));
        assertFalse(UrlUtils.isInternalErrorURL("hello world"));
    }

    @Test
    public void isHttpOrHttpsUrl() {
        assertFalse(UrlUtils.isHttpOrHttps(null));
        assertFalse(UrlUtils.isHttpOrHttps(""));
        assertFalse(UrlUtils.isHttpOrHttps("     "));
        assertFalse(UrlUtils.isHttpOrHttps("mozilla.org"));
        assertFalse(UrlUtils.isHttpOrHttps("httpstrf://example.org"));

        assertTrue(UrlUtils.isHttpOrHttps("https://www.mozilla.org"));
        assertTrue(UrlUtils.isHttpOrHttps("http://example.org"));
        assertTrue(UrlUtils.isHttpOrHttps("http://192.168.0.1"));
    }

    @Test
    public void testStripCommonSubdomains() {
        assertEquals("mozilla.org", UrlUtils.stripCommonSubdomains("mozilla.org"));
        assertEquals("mozilla.org", UrlUtils.stripCommonSubdomains("www.mozilla.org"));
        assertEquals("mozilla.org", UrlUtils.stripCommonSubdomains("m.mozilla.org"));
        assertEquals("mozilla.org", UrlUtils.stripCommonSubdomains("mobile.mozilla.org"));
        assertEquals("random.mozilla.org", UrlUtils.stripCommonSubdomains("random.mozilla.org"));
    }

    @Test
    public void isLocalizedContent() {
        assertFalse(UrlUtils.isLocalizedContent(null));
        assertFalse(UrlUtils.isLocalizedContent("mozilla.org"));
        assertFalse(UrlUtils.isLocalizedContent("http://www.mozilla.org"));
        assertTrue(UrlUtils.isLocalizedContent("about:blank"));
        assertTrue(UrlUtils.isLocalizedContent(LocalizedContent.URL_ABOUT));
        assertTrue(UrlUtils.isLocalizedContent(LocalizedContent.URL_RIGHTS));
    }

    @Test
    public void testGetURLForSearchEngineShortcut() {
        Context context = ApplicationProvider.getApplicationContext();

        assertEquals("https://www.amazon.com/gp/aw/s?k=books&sourceid=Mozilla-search&tag=mozilla-20",
                UrlUtils.getURLForSearchEngineShortcut(context, "@amazon", "books"));
        assertEquals("https://www.google.com/search?q=search%20query&ie=utf-8&oe=utf-8&client=firefox-b-1-m",
                UrlUtils.getURLForSearchEngineShortcut(context, "@google", "search query"));
        assertEquals("https://duckduckgo.com/?q=search%20query&t=fpas",
                UrlUtils.getURLForSearchEngineShortcut(context, "@duckduckgo", "search query"));
        assertEquals("https://mobile.twitter.com/search/?q=tweets",
                UrlUtils.getURLForSearchEngineShortcut(context, "@twitter", "tweets"));
        assertEquals("https://en.wikipedia.org/wiki/Special:Search?search=wiki%20search&sourceid=Mozilla-search",
                UrlUtils.getURLForSearchEngineShortcut(context, "@wikipedia", "wiki search"));

        assertNull(UrlUtils.getURLForSearchEngineShortcut(context, "@amazo", "invalid"));
        assertNull(UrlUtils.getURLForSearchEngineShortcut(context, "@googles", "invalid two"));
    }

    @Test
    public void testSplitShortcutFromQuery() {
        String[] splitQuery = UrlUtils.splitShortcutFromQuery("@amazon books search");
        assertEquals("@amazon", splitQuery[0]);
        assertEquals("books search", splitQuery[1]);

        assertNull(UrlUtils.splitShortcutFromQuery("@amazon"));
        assertNull(UrlUtils.splitShortcutFromQuery("amazon books"));
    }

}
