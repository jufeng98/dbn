package com.dbn.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DevNullStreams {
    public static final OutputStream OUTPUT_STREAM = new OutputStream() {
        @Override
        public void write(int i) throws IOException {

        }
    };

    public static final InputStream INPUT_STREAM = new InputStream() {
        @Override
        public int read() throws IOException {
            return 0;
        }
    };


}
