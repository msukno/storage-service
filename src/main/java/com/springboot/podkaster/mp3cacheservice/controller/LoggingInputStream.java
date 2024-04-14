package com.springboot.podkaster.mp3cacheservice.controller;

import java.io.IOException;
import java.io.InputStream;

public class LoggingInputStream extends InputStream {
    private final InputStream wrappedStream;
    private final long totalBytes;
    private long bytesRead = 0;

    public LoggingInputStream(InputStream wrappedStream, long totalBytes) {
        this.wrappedStream = wrappedStream;
        this.totalBytes = totalBytes;
    }

    @Override
    public int read() throws IOException {
        int byteData = wrappedStream.read();
        if (byteData != -1) {
            logProgress(1); // Only 1 byte read
        }
        return byteData;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = wrappedStream.read(b, off, len);
        if (bytesRead > 0) {
            logProgress(bytesRead);
        }
        return bytesRead;
    }

    private void logProgress(long bytesReadIncrement) {
        bytesRead += bytesReadIncrement;
        double percentage = ((double) bytesRead / totalBytes) * 100;
        System.out.printf("Uploaded %d bytes (%.2f%%)\n", bytesRead, percentage);
    }

    @Override
    public void close() throws IOException {
        wrappedStream.close();
    }

    // Implement other required methods by delegating to wrappedStream
}