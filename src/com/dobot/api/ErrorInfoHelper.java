package com.dobot.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorInfoHelper {

    private static Map<Integer, ErrorInfoBean> controllerBeans = new HashMap<>();

    private static Map<Integer, ErrorInfoBean> servoBeans = new HashMap<>();


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
            controllerBeans.put(errorInfoBeanList.get(i).getId(),errorInfoBeanList.get(i));
        }

        JSONArray alarmServoObject = JSONArray.parseArray(alarmServoBuffer.toString());
        List<ErrorInfoBean> alarmServoErrorInfoBeanList = JSONObject.parseArray(alarmServoObject.toJSONString(), ErrorInfoBean.class);
        for (int i = 0; i < alarmServoErrorInfoBeanList.size(); i++) {
            servoBeans.put(alarmServoErrorInfoBeanList.get(i).getId(),alarmServoErrorInfoBeanList.get(i));
        }

    }

    public static ErrorInfoHelper getInstance(){
       return instance;
    }

    public boolean parseResult(String strResult)
    {
        int iBegPos = strResult.indexOf('{');
        if (iBegPos < 0)
        {
            return false;
        }
        int iEndPos = strResult.indexOf('}',
                iBegPos + 1);
        if (iEndPos < 0)
        {
            return false;
        }
        boolean bOk = strResult.startsWith("0,");
        if( iEndPos - iBegPos <=1){
            return bOk;
        }
        strResult = strResult.substring(iBegPos + 1, iEndPos);
        if (strResult == null || strResult.isEmpty())
        {
            return bOk;
        }
        StringBuilder sb = new StringBuilder();
        String[] all = strResult.split( "," , 0);

        JSONArray jsonArray = JSONArray.parseArray(strResult);

        for(int i=0 ;i<jsonArray.size() ;i++){

            JSONArray array = jsonArray.toJavaObject(JSONArray.class);
            for(int j=0 ;j<array.size(); j++){
                ErrorInfoBean bean = null;
                if(i == 0){
                    bean = findController(array.toJavaObject(Integer.class));

                }else {
                    bean = findServo(array.toJavaObject(Integer.class));
                }
                if (null != bean)
                {
                    sb.append("ID:" + bean.getId() + "\r\n");
                    sb.append("Type:"+bean.getType() + "\r\n");
                    sb.append("Level:"+bean.getLevel() + "\r\n");
                    sb.append("Solution:"+bean.getEn().solution + "\r\n");
                }
            }

        }

        if (sb.length() > 0)
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");//设置日期格式
            String strTime = "Time Stamp:"+df.format(new Date());// new Date()为获取当前系统时间
            Logger.instance.error(strTime+"\r\n"+sb);
        }
        return bOk;
    }


    public  ErrorInfoBean findController(int id)
    {
        if (controllerBeans.containsKey(id))
        {
            return controllerBeans.get(id);
        }
        return null;
    }
    public  ErrorInfoBean findServo(int id)
    {
        if (servoBeans.containsKey(id))
        {
            return servoBeans.get(id);
        }
        return null;
    }


}