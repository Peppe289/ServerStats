package com.peppe289.server_stats.Model;

public class DiskInfo {
    private final String name;
    private final long size;

    public DiskInfo(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }
}
