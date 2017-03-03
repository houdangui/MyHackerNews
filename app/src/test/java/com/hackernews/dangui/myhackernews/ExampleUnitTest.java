package com.hackernews.dangui.myhackernews;

import com.hackernews.dangui.myhackernews.api.HackerNewsApi;
import com.hackernews.dangui.myhackernews.util.Utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void LongArrayToStringVerify() {
        String strKids = "[13779451, 13779513, 13779002, 13779836, 13778843, 13778931, 13778696]";
        Long[] kids = new Long[]{13779451L, 13779513L, 13779002L, 13779836L, 13778843L, 13778931L, 13778696L};
        assertEquals(strKids, Utils.longArrayToString(kids));

        Long[] kidsFromStr = Utils.stringToLongArray(strKids);
        assertArrayEquals(kids, kidsFromStr);
    }
}