package com.dbn.connection.config.tns;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TnsProfilePatternTest {

    @Test
    public void get() {
        Pattern pattern = TnsProfilePattern.INSTANCE.get();

        Matcher matcher = pattern.matcher("dcidbn0001_high = (description= (retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=adb.eu-zurich-1.oraclecloud.com))(connect_data=(service_name=g47875f42217f9e_dcidbn0001_high.adb.oraclecloud.com))(security=(ssl_server_dn_match=yes)))");
        boolean matches = matcher.matches();
        Assert.assertTrue(matches);
    }
}