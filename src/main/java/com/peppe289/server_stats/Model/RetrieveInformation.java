package com.peppe289.server_stats.Model;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class RetrieveInformation {
    private final HardwareAbstractionLayer hal;
    private final OperatingSystem os;

    public RetrieveInformation() {
        SystemInfo si = new SystemInfo();
        hal = si.getHardware();
        os = si.getOperatingSystem();
    }

    public CentralProcessor getCPUInfo() {
        return hal.getProcessor();
    }

    public String getOSInfo() {
        return os.toString();
    }

    public String getMotherboardInfo() {
        return hal.getComputerSystem().getBaseboard().toString();
    }

    public String getBIOSInfo() {
        return hal.getComputerSystem().getFirmware().toString();
    }

    // TODO: check this function. I don't like this implementation.
    public double getCPULoad() {
        CentralProcessor processor = hal.getProcessor();

        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            // wait 1 second for get information about CPU load.
            Thread.sleep(1000);
        } catch (InterruptedException ignore) {
            // ignore for now
        }

        return processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
    }

    public MemoryInfo getMemoryInfo() {
        return new MemoryInfo(hal.getMemory());
    }

    public List<HWDiskStore> getDiskInfo() {
        return hal.getDiskStores();
    }

    public NetWorkInfo[] getNetWorkInfo() {
        // skipp if no network traffic is being received or sent
        // this interface is down.
        return hal.getNetworkIFs().stream().filter(
                        // skipp if no network traffic is being received or sent
                        // this interface is down.
                        NetworkIF::isConnectorPresent)
                .map(net -> {
                    net.updateAttributes();
                    long bytesReceived1 = net.getBytesRecv();
                    long bytesSent1 = net.getBytesSent();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    net.updateAttributes();

                    long bytesReceived2 = net.getBytesRecv();
                    long bytesSent2 = net.getBytesSent();

                    long receivedPerSecond = bytesReceived2 - bytesReceived1;
                    long sentPerSecond = bytesSent2 - bytesSent1;

                    return new NetWorkInfo(net.getName(), sentPerSecond, receivedPerSecond);
                }).toArray(NetWorkInfo[]::new);
    }

    public List<HashMap<String, Object>> getDiskSpace() {
        List<HashMap<String, Object>> disksSpace = new java.util.ArrayList<>();
        // Check all disk
        File[] roots = File.listRoots();
        for (File root : roots) {
            HashMap<String, Object> disk = new HashMap<>();
            disk.put("root", root.getAbsolutePath());
            disk.put("total", root.getTotalSpace() /  (1024.0 * 1024.0));
            disk.put("free", root.getFreeSpace() / (1024.0 * 1024.0));
            disksSpace.add(disk);
        }

        return disksSpace;
    }
}
