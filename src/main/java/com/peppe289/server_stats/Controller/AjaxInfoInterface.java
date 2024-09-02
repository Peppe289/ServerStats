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

    private HashMap<String, Object> handlerOther() {
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

    private HashMap<String, Object> handlerMain() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        RetrieveInformation retrieveInformation = new RetrieveInformation();

        HashMap<String, Object> result = new HashMap<>();

        result.put("Memory", retrieveInformation.getMemoryInfo());
        result.put("CPU", retrieveInformation.getCPULoad());

        return result;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = null;

        String reason = request.getParameter("reason");

        if (reason != null && reason.equals("main"))
            json = objectMapper.writeValueAsString(handlerMain());
        else
            json = objectMapper.writeValueAsString(handlerOther());

        response.setContentType("application/json");
        response.getWriter().print(json);
        response.getWriter().flush();
    }

    public void destroy() {
    }
}