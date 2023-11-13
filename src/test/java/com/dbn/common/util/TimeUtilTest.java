package com.dbn.common.util;

import com.dbn.common.util.TimeUtil;
import org.junit.Assert;
import org.junit.Test;

import static com.dbn.common.util.TimeUtil.Millis.*;

public class TimeUtilTest {

    @Test
    public void presentableDuration() {
        Assert.assertEquals("1h", logged(TimeUtil.presentableDuration(ONE_HOUR, true)));
        Assert.assertEquals("1h 1m", logged(TimeUtil.presentableDuration(ONE_HOUR + ONE_MINUTE, true)));
        Assert.assertEquals("1h 35m", logged(TimeUtil.presentableDuration(ONE_HOUR + ONE_MINUTE * 35, true)));
        Assert.assertEquals("2h 35m", logged(TimeUtil.presentableDuration(ONE_HOUR * 2 + ONE_MINUTE * 35, true)));
        Assert.assertEquals("1m", logged(TimeUtil.presentableDuration(ONE_MINUTE, true)));
        Assert.assertEquals("1m 45s", logged(TimeUtil.presentableDuration(ONE_MINUTE + ONE_SECOND * 45, true)));
        Assert.assertEquals("3m 45s", logged(TimeUtil.presentableDuration(ONE_MINUTE * 3 + ONE_SECOND * 45, true)));
        Assert.assertEquals("1000 ms", logged(TimeUtil.presentableDuration(ONE_SECOND, true)));
        Assert.assertEquals("1456 ms", logged(TimeUtil.presentableDuration(ONE_SECOND + 456, true)));
        Assert.assertEquals("3456 ms", logged(TimeUtil.presentableDuration(ONE_SECOND * 3 + 456, true)));
        Assert.assertEquals("7s 456ms", logged(TimeUtil.presentableDuration(ONE_SECOND * 7 + 456, true)));
        Assert.assertEquals("one hour", logged(TimeUtil.presentableDuration(ONE_HOUR, false)));
        Assert.assertEquals("one hour and one minute", logged(TimeUtil.presentableDuration(ONE_HOUR + ONE_MINUTE, false)));
        Assert.assertEquals("one hour and 35 minutes", logged(TimeUtil.presentableDuration(ONE_HOUR + ONE_MINUTE * 35, false)));
        Assert.assertEquals("2 hours and 35 minutes", logged(TimeUtil.presentableDuration(ONE_HOUR * 2 + ONE_MINUTE * 35, false)));
        Assert.assertEquals("one minute", logged(TimeUtil.presentableDuration(ONE_MINUTE, false)));
        Assert.assertEquals("one minute and 45 seconds", logged(TimeUtil.presentableDuration(ONE_MINUTE + ONE_SECOND * 45, false)));
        Assert.assertEquals("3 minutes and 45 seconds", logged(TimeUtil.presentableDuration(ONE_MINUTE * 3 + ONE_SECOND * 45, false)));
        Assert.assertEquals("1000 ms", logged(TimeUtil.presentableDuration(ONE_SECOND, false)));
        Assert.assertEquals("1456 ms", logged(TimeUtil.presentableDuration(ONE_SECOND + 456, false)));
        Assert.assertEquals("3456 ms", logged(TimeUtil.presentableDuration(ONE_SECOND * 3 + 456, false)));
        Assert.assertEquals("7 seconds and 456 ms", logged(TimeUtil.presentableDuration(ONE_SECOND * 7 + 456, false)));
            }
    
    private static String logged(String val) {
        System.out.println(val);
        return val;
    }
}