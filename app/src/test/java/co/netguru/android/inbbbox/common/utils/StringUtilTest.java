package co.netguru.android.inbbbox.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {
    @Test
    public void isBlankTest(){
        //blank char sequences
        Assert.assertTrue(StringUtil.isBlank(null));
        Assert.assertTrue(StringUtil.isBlank(""));
        Assert.assertTrue(StringUtil.isBlank(" "));
        Assert.assertTrue(StringUtil.isBlank("    "));
        //not blank sequences
        Assert.assertFalse(StringUtil.isBlank("a"));
        Assert.assertFalse(StringUtil.isBlank(" a"));
        Assert.assertFalse(StringUtil.isBlank("  `  "));
    }

}