package com.peppe289.server_stats.Model;

import oshi.hardware.GlobalMemory;


public class MemoryInfo {
    private final long physicalMemorySize;
    private final long freePhysicalMemory;

    public MemoryInfo(GlobalMemory memory) {
        // get in MB
        this.physicalMemorySize = memory.getTotal() / 1024 / 1024;
        this.freePhysicalMemory = memory.getAvailable() / 1024 / 1024;
    }

    public long getPhysicalMemorySize() {
        return physicalMemorySize;
    }

    public long getFreePhysicalMemory() {
        return freePhysicalMemory;
    }
}
