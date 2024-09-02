package com.peppe289.server_stats.Model;

public class NetWorkInfo {
    private final String name;
    private final long bytesSent;
    private final long bytesReceived;

    public NetWorkInfo(String name, long bytesSent, long bytesReceived) {
        this.name = name;
        this.bytesSent = bytesSent;
        this.bytesReceived = bytesReceived;
    }

    public String getName() {
        return name;
    }

    public long getBytesSent() {
        return bytesSent;
    }

    public long getBytesReceived() {
        return bytesReceived;
    }
}
