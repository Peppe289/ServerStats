package com.peppe289.server_stats.Model;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

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
}
