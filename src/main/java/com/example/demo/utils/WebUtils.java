package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class WebUtils {

    /**
     * 获取远程ip物理地址
     * @param sip
     * @return
     */
    public static String getRemoteMac(String sip) {
        ExecutorService exec = Executors.newFixedThreadPool(1);
        final UdpGetClientMacAddr umac;
        try {
            umac = new UdpGetClientMacAddr(sip);
            //---长时间获取不到MAC地址则放弃
            Callable<String> call = () ->{
                    return umac.GetRemoteMacAddr();
                }
            ;

            Future<String> future = exec.submit(call);
            String smac = future.get(1000 * 1, TimeUnit.MILLISECONDS);
            return smac;
        } catch (Exception ex) {
            log.info("获取MAC地址超时");
            ex.printStackTrace();

        } finally {
            // 关闭线程池
            exec.shutdown();
        }
        return null;
    }
    /**
     * 获取ip
     * @param request
     * @return
     * @throws Exception
     */
    public static String getIp(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                int index = ip.indexOf(",");
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip =  request.getRemoteAddr();
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    public static String getMacAddrByIp(String ip) {
        String macAddr = null;
        try {
            Process process = Runtime.getRuntime().exec("nbtstat -a " + ip);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            Pattern pattern = Pattern.compile("([A-F0-9]{2}-){5}[A-F0-9]{2}");
            Matcher matcher;
            for (String strLine = br.readLine(); strLine != null;
                 strLine = br.readLine()) {
                matcher = pattern.matcher(strLine);
                if (matcher.find()) {
                    macAddr = matcher.group();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return macAddr;
    }
    public static void main(String args[]) throws Exception{
        System.out.println(getMacAddrByIp("139.159.243.116"));
    }
}
