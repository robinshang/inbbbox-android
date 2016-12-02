package co.netguru.android.inbbbox.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest {
    @Test
    public void isBlankTest(){
        //blank char sequences
        Assert.assertTrue(StringUtils.isBlank(null));
        Assert.assertTrue(StringUtils.isBlank(""));
        Assert.assertTrue(StringUtils.isBlank(" "));
        Assert.assertTrue(StringUtils.isBlank("    "));
        //not blank sequences
        Assert.assertFalse(StringUtils.isBlank("a"));
        Assert.assertFalse(StringUtils.isBlank(" a"));
        Assert.assertFalse(StringUtils.isBlank("  `  "));
    }

}