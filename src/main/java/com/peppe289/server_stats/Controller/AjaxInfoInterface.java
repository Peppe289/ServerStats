package com.peppe289.server_stats.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peppe289.server_stats.Model.DiskInfo;
import com.peppe289.server_stats.Model.MemoryInfo;
import com.peppe289.server_stats.Model.NetWorkInfo;
import com.peppe289.server_stats.Model.RetrieveInformation;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "info-api", value = "/info-api")
public class AjaxInfoInterface extends HttpServlet {
    private String message;
    private ObjectMapper objectMapper;

    public void init() {
        objectMapper = new ObjectMapper();
    }

    private HashMap<String, Object> newHandler() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();
        RetrieveInformation retrieveInformation = new RetrieveInformation();

        HashMap<String, Object> result = new HashMap<>();

        result.put("CPU", retrieveInformation.getCPUInfo());
        result.put("OS", retrieveInformation.getOSInfo());
        result.put("Memory", retrieveInformation.getMemoryInfo());
        result.put("Disk", retrieveInformation.getDiskInfo());
        result.put("Network", retrieveInformation.getNetWorkInfo());

        return result;

    }

    private HashMap<String, Object> handler() throws InterruptedException {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();
        // info data
        List<DiskInfo> diskList = new ArrayList<>();
        List<NetWorkInfo> netWorkInfo = new ArrayList<>();

        HashMap<String, Object> result = new HashMap<>();

        // disk information
        hal.getDiskStores().forEach(disk -> {
            DiskInfo diskItem = new DiskInfo(disk.getName(), disk.getSize());
            diskList.add(diskItem);
        });

        // Get all network interfaces
        for (NetworkIF net : hal.getNetworkIFs()) {

            // skipp if no network traffic is being received or sent
            // this interface is down.
            if (!net.isConnectorPresent()) {
                continue;
            }

            net.updateAttributes();
            long bytesReceived1 = net.getBytesRecv();
            long bytesSent1 = net.getBytesSent();

            Thread.sleep(1000);

            net.updateAttributes();

            long bytesReceived2 = net.getBytesRecv();
            long bytesSent2 = net.getBytesSent();

            long receivedPerSecond = bytesReceived2 - bytesReceived1;
            long sentPerSecond = bytesSent2 - bytesSent1;

            // bytes per second
            netWorkInfo.add(new NetWorkInfo(net.getName(), sentPerSecond, receivedPerSecond));
        }

        result.put("CPU", hal.getProcessor());
        result.put("OS", os.toString());
        result.put("Memory", new MemoryInfo(hal.getMemory()));
        result.put("Disk", diskList);
        result.put("Network", netWorkInfo);

        return result;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = null;

        json = objectMapper.writeValueAsString(newHandler());

        response.setContentType("application/json");
        response.getWriter().print(json);
    }

    public void destroy() {
    }
}