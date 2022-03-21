package com.dobot.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorInfoHelper {
    private static Map<Integer, ErrorInfoBean> allErrorBeans = new HashMap<>();

    private static ErrorInfoHelper instance=new ErrorInfoHelper();
    private ErrorInfoHelper(){

        String projectPath = System.getProperty("user.dir");
        String alarmControllerPath = projectPath+"\\resource\\alarm_controller.json";
        String alarmServoPath = projectPath+"\\resource\\alarm_servo.json";
        File file = new File(alarmControllerPath);
        StringBuffer buffer = new StringBuffer();
        InputStreamReader read;
        try {
            read = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                buffer.append(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File alarmServoFile = new File(alarmServoPath);
        StringBuffer alarmServoBuffer = new StringBuffer();
        try {
            read = new InputStreamReader(new FileInputStream(alarmServoFile));
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                alarmServoBuffer.append(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray objects = JSONArray.parseArray(buffer.toString());
        List<ErrorInfoBean> errorInfoBeanList = JSONObject.parseArray(objects.toJSONString(), ErrorInfoBean.class);
        for (int i = 0; i < errorInfoBeanList.size(); i++) {
            allErrorBeans.put(errorInfoBeanList.get(i).getId(),errorInfoBeanList.get(i));
        }

        JSONArray alarmServoObject = JSONArray.parseArray(alarmServoBuffer.toString());
        List<ErrorInfoBean> alarmServoErrorInfoBeanList = JSONObject.parseArray(alarmServoObject.toJSONString(), ErrorInfoBean.class);
        for (int i = 0; i < alarmServoErrorInfoBeanList.size(); i++) {
            allErrorBeans.put(errorInfoBeanList.get(i).getId(),errorInfoBeanList.get(i));
        }
        System.out.println("allErrorBeans");

    }

    public static ErrorInfoHelper getInstance(){
       return instance;
    }


    public ErrorInfoBean find(int id)
    {
        if (allErrorBeans.containsKey(id))
        {
            return allErrorBeans.get(id);
        }
        return null;
    }


}