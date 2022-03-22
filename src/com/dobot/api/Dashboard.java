package com.dobot.api;



import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Dashboard {
    private Socket socketClient = new Socket();

    private static String SEND_ERROR = ":send error";

    private String ip = "";


    public boolean connect(String ip,int port){
        boolean ok = false;
        try {
            socketClient = new Socket(ip,port);
            this.ip = ip;
            Logger.instance.log("Dashboard connect success");
            ok = true;
        } catch (Exception e) {
            Logger.instance.log("Connect failed:" + e.getMessage());
        }
        return ok;
    }

    public void disconnect(){
        if(!socketClient.isClosed()){
            try {
                socketClient.shutdownOutput();
                socketClient.shutdownInput();
                socketClient.close();
                Logger.instance.log("Dashboard closed");
            } catch (Exception e) {
                Logger.instance.log("Dashboard Close Socket Exception:" + e.getMessage());
            }
        }else {
            Logger.instance.log("this ip is not connected");
        }
    }

    public String DigitalOutputs(int index, boolean status)
    {
        if (socketClient.isClosed())
        {
            Logger.instance.log("device does not connected!!!");
            return "device does not connected!!!";
        }
        int statusInt = status ? 1 : 0;
        String str = "DO("+index+","+statusInt+")";
        if (!sendData(str))
        {
            return str + ":send error";
        }

        return waitReply(5000);
    }


    public String clearError(){
        if(socketClient.isClosed()){
            Logger.instance.log("device does not connected!!!");
            return "device does not connected!!!";
        }
        String str = "ClearError()";
        if(!sendData(str)){
            return str + SEND_ERROR;
        }

        return waitReply(5000);
    }

    public String PowerOn(){
        if(socketClient.isClosed()){
            Logger.instance.log("device does not connected!!!");
            return "device does not connected!!!";
        }
        String str = "PowerOn()";
        if(!sendData(str)){
            return str + SEND_ERROR;
        }

        return waitReply(15000);
    }

    public String PowerOff() {
        return emergencyStop();
    }

    public String emergencyStop()
    {
        if (socketClient.isClosed())
        {
            Logger.instance.log("device does not connected!!!");
            return "device does not connected!!!";
        }
        String str = "EmergencyStop()";
        if(!sendData(str)){
            return str + SEND_ERROR;
        }
        return waitReply(15000);
    }

    public String enableRobot()
    {
        if (socketClient.isClosed())
        {
            Logger.instance.log("device does not connected!!!");
            return "device does not connected!!!";
        }

        String str = "EnableRobot()";
        if(!sendData(str)){
            return str + SEND_ERROR;
        }

        return waitReply(20000);
    }

    public String disableRobot()
    {
        if (socketClient.isClosed())
        {
            Logger.instance.log("device does not connected!!!");
            return "device does not connected!!!";
        }

        String str = "DisableRobot()";
        if(sendData(str)){
            return str + SEND_ERROR;
        }

        return waitReply(20000);
    }

    public String resetRobot(){
        if (socketClient.isClosed())
        {
            Logger.instance.log("device does not connected!!!");
            return "device does not connected!!!";
        }

        String str = "ResetRobot()";
        if(!sendData(str)){
            return str + SEND_ERROR;
        }

        return waitReply(20000);
    }

    public String speedFactor(int ratio)
    {
        if (socketClient.isClosed())
        {
            Logger.instance.log("device does not connected!!!");
            return "device does not connected!!!";
        }
        String str ="SpeedFactor(" + ratio +")";
        if(!sendData(str)){
            return str + SEND_ERROR;
        }
        return waitReply(5000);
    }

    public boolean sendData(String str){
        try {
            Logger.instance.log("Send to:" +this.ip+":"+socketClient.getPort()+":"+str);
            socketClient.getOutputStream().write((str).getBytes());
        } catch (IOException e) {
            Logger.instance.log("Exception:" + e.getMessage());
            return false;
        }
        return true;
    }

    public String waitReply(int timeout){
        String reply = "";
        try {
            if(socketClient.getSoTimeout() != timeout){
                socketClient.setSoTimeout(timeout);
            }
            byte[] buffer = new byte[1024];				//缓冲
            int len = socketClient.getInputStream().read(buffer);//每次读取的长度（正常情况下是1024，最后一次可能不是1024，如果传输结束，返回-1）
            reply = new String(buffer,0,len,"UTF-8");
            parseResult(reply);
            Logger.instance.log("Receive from:"+this.ip+":"+socketClient.getPort()+":"+reply);

        } catch (IOException e) {
            Logger.instance.log("Exception:"+e.getMessage());
            return reply;
        }
        return reply;
    }

    private boolean parseResult(String strResult)
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
        if(iBegPos + 1>= iEndPos){
            return bOk;
        }
        strResult = strResult.substring(iBegPos + 1, iEndPos);
        if (strResult == null || strResult.isEmpty())
        {
            return bOk;
        }
        StringBuilder sb = new StringBuilder();
        String[] all = strResult.split( "," , 0);

        for(int i=0 ;i<all.length ;i++){
            ErrorInfoBean bean = ErrorInfoHelper.getInstance().find(Integer.valueOf(all[i]));
            if (null != bean)
            {
                sb.append("ID:" + bean.getId() + "\r\n");
                sb.append("Type:"+bean.getType() + "\r\n");
                sb.append("Level:"+bean.getLevel() + "\r\n");
                sb.append("Solution:"+bean.getEn().solution + "\r\n");
            }
        }

        if (sb.length() > 0)
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");//设置日期格式
            String strTime = "Time Stamp:"+df.format(new Date());// new Date()为获取当前系统时间
        }
        return bOk;
    }

}