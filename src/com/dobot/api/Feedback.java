package com.dobot.api;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;


public class Feedback {
    public Socket socketClient = new Socket();

    private static String SEND_ERROR = ":send error";

    Thread thread;

    private String ip = "";

    private int port;


    public boolean connect(String ip, Integer port)
    {
        boolean bOk = false;
        try
        {
            this.ip = ip;
            this.port = port;


            thread= new Thread(){
                @Override
                public void run(){
                    try {
                        onRecvData();
                        return;
                    } catch (Exception e) {
                        Logger.instance.log(e.getMessage());
                    }

                }
            };


            thread.start();
            Logger.instance.log("Feedback connect success");
            bOk = true;
        }
        catch (Exception ex)
        {
            Logger.instance.log("Connect failed:" + ex.toString());
        }
        return bOk;
    }

    public short MessageSize;//消息字节总长度

    public short[] Reserved1;//保留位

    public long DigitalInputs; //数字输入
    public long DigitalOutputs; //数字输出
    public int RobotMode; //机器人模式
    public long TimeStamp; //时间戳（单位ms）

    public long Reserved2; //保留位

    public long TestValue; //内存结构测试标准值  0x0123 4567 89AB CDEF

    public double Reserved3; //保留位

    public double SpeedScaling;//速度比例
    public double LinearMomentumNorm; //机器人当前动量
    public double VMain; //控制板电压
    public double VRobot; //机器人电压
    public double IRobot; //机器人电流

    public double Reserved4; //保留位
    public double Reserved5; //保留位

    public double[] ToolAccelerometerValues; //TCP加速度
    public double[] ElbowPosition; //肘位置
    public double[] ElbowVelocity; //肘速度

    public double[] QTarget; //目标关节位置
    public double[] QdTarget; //目标关节速度
    public double[] QddTarget; //目标关节加速度
    public double[] ITarget; //目标关节加速度
    public double[] MTarget; //目标关节电流
    public double[] QActual; //实际关节位置
    public double[] QdActual; //实际关节速度
    public double[] IActual; //实际关节电流
    public double[] IControl; //TCP传感器力值
    public double[] ToolVectorActual; //TCP笛卡尔实际坐标值
    public double[] TCPSpeedActual; //TCP笛卡尔实际速度值
    public double[] TCPForce; //TCP力值
    public double[] ToolVectorTarget; //TCP笛卡尔目标坐标值
    public double[] TCPSpeedTarget; //TCP笛卡尔目标速度值
    public double[] MotorTempetatures; //关节温度
    public double[] JointModes; //关节控制模式
    public double[] VActual; //关节电压

    public byte[] Handtype; //手系
    public byte User; //用户坐标
    public byte Tool; //工具坐标
    public byte RunQueuedCmd; //算法队列运行标志
    public byte PauseCmdFlag; //算法队列暂停标志
    public byte VelocityRatio; //关节速度比例(0~100)
    public byte AccelerationRatio; //关节加速度比例(0~100)
    public byte JerkRatio; //关节加加速度比例(0~100)
    public byte XYZVelocityRatio; //笛卡尔位置速度比例(0~100)
    public byte RVelocityRatio; //笛卡尔姿态速度比例(0~100)
    public byte XYZAccelerationRatio; //笛卡尔位置加速度比例(0~100)
    public byte RAccelerationRatio; //笛卡尔姿态加速度比例(0~100)
    public byte XYZJerkRatio; //笛卡尔位置加加速度比例(0~100)
    public byte RJerkRatio; //笛卡尔姿态加加速度比例(0~100)

    public byte BrakeStatus;//机器人抱闸状态
    public byte EnableStatus; //机器人使能状态
    public byte DragStatus; //机器人拖拽状态
    public byte RunningStatus; //机器人运行状态
    public byte ErrorStatus; //机器人报警状态
    public byte JogStatus; //机器人点动状态
    public byte RobotType; //机器类型
    public byte DragButtonSignal; //按钮板拖拽信号
    public byte EnableButtonSignal; //按钮板使能信号
    public byte RecordButtonSignal; //按钮板录制信号
    public byte ReappearButtonSignal; //按钮板复现信号
    public byte JawButtonSignal; //按钮板夹爪控制信号
    public byte SixForceOnline; //六维力在线状态

    public byte[] Reserved6; //保留位

    public double[] MActual; //实际扭矩

    public double Load; //负载重量kg
    public double CenterX; //X方向偏心距离mm
    public double CenterY; //Y方向偏心距离mm
    public double CenterZ; //Z方向偏心距离mm
    public double[] UserValue; //用户坐标值
    public double[] Tools;//工具坐标值
    public double TraceIndex; //轨迹复现运行索引
    public double[] SixForceValue; //当前六维力数据原始值
    public double[] TargetQuaternion; //[qw,qx,qy,qz] 目标四元数
    public double[] ActualQuaternion; //[qw,qx,qy,qz]  实际四元数

    public byte[] Reserved7; //保留位


    public boolean DataHasRead;

    public String IP;
    public int Port;

    public Feedback()
    {
        this.MessageSize = 0;

        this.Reserved1 = new short[3];

        this.DigitalInputs = 0;
        this.DigitalOutputs = 0;
        this.RobotMode = 0;
        this.TimeStamp = 0;

        this.Reserved2 = 0;
        this.TestValue = 0;
        this.Reserved3 = 0;

        this.SpeedScaling = 0;
        this.LinearMomentumNorm = 0;
        this.VMain = 0;
        this.VRobot = 0;
        this.IRobot = 0;

        this.Reserved4 = 0;
        this.Reserved5 = 0;

        this.ToolAccelerometerValues = new double[3];
        this.ElbowPosition = new double[3];
        this.ElbowVelocity = new double[3];

        this.QTarget = new double[6];
        this.QdTarget = new double[6];
        this.QddTarget = new double[6];
        this.ITarget = new double[6];
        this.MTarget = new double[6];
        this.QActual = new double[6];
        this.QdActual = new double[6];
        this.IActual = new double[6];
        this.IControl = new double[6];
        this.ToolVectorActual = new double[6];
        this.TCPSpeedActual = new double[6];
        this.TCPForce = new double[6];
        this.ToolVectorTarget = new double[6];
        this.TCPSpeedTarget = new double[6];
        this.MotorTempetatures = new double[6];
        this.JointModes = new double[6];
        this.VActual = new double[6];

        this.Handtype = new byte[4];
        this.User = 0;
        this.Tool = 0;
        this.RunQueuedCmd = 0;
        this.PauseCmdFlag = 0;
        this.VelocityRatio = 0;
        this.AccelerationRatio = 0;
        this.JerkRatio = 0;
        this.XYZVelocityRatio = 0;
        this.RVelocityRatio = 0;
        this.XYZAccelerationRatio = 0;
        this.RAccelerationRatio = 0;
        this.XYZJerkRatio = 0;
        this.RJerkRatio = 0;

        this.BrakeStatus = 0;
        this.EnableStatus = 0;
        this.DragStatus = 0;
        this.RunningStatus = 0;
        this.ErrorStatus = 0;
        this.JogStatus = 0;
        this.RobotType = 0;
        this.DragButtonSignal = 0;
        this.EnableButtonSignal = 0;
        this.RecordButtonSignal = 0;
        this.ReappearButtonSignal = 0;
        this.JawButtonSignal = 0;
        this.SixForceOnline = 0;

        this.Reserved6 = new byte[82];

        this.MActual = new double[6];
        this.Load = 0;
        this.CenterX = 0;
        this.CenterY = 0;
        this.CenterZ = 0;
        this.UserValue = new double[6];
        this.Tools = new double[6];
        this.TraceIndex = 0;
        this.SixForceValue = new double[6];
        this.TargetQuaternion = new double[4];
        this.ActualQuaternion = new double[4];

        this.Reserved7 = new byte[24];
    }


    /// <summary>
    /// 断开连接
    /// </summary>
    public void disconnect()
    {
        if (!socketClient.isClosed())
        {
            try
            {
                socketClient.shutdownOutput();
                socketClient.shutdownInput();
                socketClient.close();
                Logger.instance.log("Feedback closed");
            }
            catch(Exception ex)
            {
                Logger.instance.log("Feedback Close Socket Exception:" + ex.toString());
            }
        }
        if ( null!= thread && thread.isAlive())
        {
            try
            {
                thread.interrupt();
                thread = null;
            }
            catch (Exception ex)
            {
                Logger.instance.log("close thread:" + ex.toString());
            }
        }
    }

    /// <summary>
    /// 接收返回的数据并解析处理
    /// </summary>
    public void onRecvData() throws IOException {
        byte[] buffer = new byte[4320];//1440*3
        socketClient = new Socket(ip,port);
        int iHasRead = 0;
        while (!socketClient.isClosed())
        {
            try
            {
                int iRet = socketClient.getInputStream().read(buffer,0, buffer.length);
                if (iRet <= 0)
                {
                    continue;
                }
                iHasRead += iRet;
                if (iHasRead < 1440)
                {
                    continue;
                }

                boolean bHasFound = false;//是否找到数据包头了
                for (int i=0; i<iHasRead; ++i)
                {
//                    //找到消息头
//                    int iMsgSize = buffer[i+1];
//                    iMsgSize <<= 8;
//                    iMsgSize |= buffer[i];
//                    iMsgSize &= 0x00FFFF;
//                    if (1440 != iMsgSize)
//                    {
//                        continue;
//                    }
                    //校验
                    long checkValue = BitConverter.toLong(buffer, i + 48);
                    if (0x0123456789ABCDEFL== checkValue)
                    {//找到了校验值
                        bHasFound = true;
                        if (i != 0)
                        {//说明存在粘包，要把前面的数据清理掉
                            iHasRead = iHasRead - i;
                            buffer = Arrays.copyOf(buffer, buffer.length-i);
                        }
                        break;
                    }
                }
                if (!bHasFound)
                {//如果没找到头，判断数据长度是不是快超过了总长度，超过了，说明数据全都有问题，删掉
                    if (iHasRead >= buffer.length) iHasRead = 0;
                    continue;
                }
                //再次判断字节数是否够
                if (iHasRead < 1440)
                {
                    continue;
                }
                iHasRead = iHasRead - 1440;
                //按照协议的格式解析数据
                ParseData(buffer);
                Arrays.copyOf(buffer, buffer.length - 1440);
            }
            catch (Exception ex)
            {
                Logger.instance.log("recv thread:" + ex);
            }
        }
        return;
    }

    private void ParseData(byte[] buffer)
    {
        int iStartIndex = 0;

        this.MessageSize = BitConverter.toShort(buffer, iStartIndex); //unsigned short
        iStartIndex += 2;

        for (int i = 0; i < this.Reserved1.length; ++i)
        {
            this.Reserved1[i] = BitConverter.toShort(buffer, iStartIndex);
            iStartIndex += 2;
        }

        this.DigitalInputs = BitConverter.toLong(buffer, iStartIndex);
        iStartIndex += 8;

        this.DigitalOutputs = BitConverter.toLong(buffer, iStartIndex);
        iStartIndex += 8;

        this.RobotMode = BitConverter.toInt(buffer, iStartIndex);
        iStartIndex += 8;

        this.TimeStamp = BitConverter.toLong(buffer, iStartIndex);
        iStartIndex += 8;

        this.Reserved2 = BitConverter.toLong(buffer, iStartIndex);
        iStartIndex += 8;

        this.TestValue = BitConverter.toLong(buffer, iStartIndex);
        iStartIndex += 8;

        this.Reserved3 = BitConverter.toDouble(buffer, iStartIndex);
        iStartIndex += 8;

        this.SpeedScaling = BitConverter.toDouble(buffer, iStartIndex);
        iStartIndex += 8;

        this.LinearMomentumNorm = BitConverter.toDouble(buffer, iStartIndex);
        iStartIndex += 8;

        this.VMain = BitConverter.toDouble(buffer, iStartIndex);
        iStartIndex += 8;

        this.VRobot = BitConverter.toDouble(buffer, iStartIndex);
        iStartIndex += 8;

        this.IRobot = BitConverter.toDouble(buffer, iStartIndex);
        iStartIndex += 8;

        this.Reserved4 = BitConverter.toDouble(buffer, iStartIndex);
        iStartIndex += 8;

        this.Reserved5 = BitConverter.toDouble(buffer, iStartIndex);
        iStartIndex += 8;

        for (int i = 0; i < this.ToolAccelerometerValues.length; ++i)
        {
            this.ToolAccelerometerValues[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.ElbowPosition.length; ++i)
        {
            this.ElbowPosition[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.ElbowVelocity.length; ++i)
        {
            this.ElbowVelocity[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.QTarget.length; ++i)
        {
            this.QTarget[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.QdTarget.length; ++i)
        {
            this.QdTarget[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.QddTarget.length; ++i)
        {
            this.QddTarget[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.ITarget.length; ++i)
        {
            this.ITarget[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.MTarget.length; ++i)
        {
            this.MTarget[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.QActual.length; ++i)
        {
            this.QActual[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.QdActual.length; ++i)
        {
            this.QdActual[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.IActual.length; ++i)
        {
            this.IActual[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.IControl.length; ++i)
        {
            this.IControl[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.ToolVectorActual.length; ++i)
        {
            this.ToolVectorActual[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.TCPSpeedActual.length; ++i)
        {
            this.TCPSpeedActual[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.TCPForce.length; ++i)
        {
            this.TCPForce[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.ToolVectorTarget.length; ++i)
        {
            this.ToolVectorTarget[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.TCPSpeedTarget.length; ++i)
        {
            this.TCPSpeedTarget[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.MotorTempetatures.length; ++i)
        {
            this.MotorTempetatures[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.JointModes.length; ++i)
        {
            this.JointModes[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.VActual.length; ++i)
        {
            this.VActual[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.Handtype.length; ++i)
        {
            this.Handtype[i] = buffer[iStartIndex];
            iStartIndex += 1;
        }

        this.User = buffer[iStartIndex];
        iStartIndex += 1;

        this.Tool = buffer[iStartIndex];
        iStartIndex += 1;

        this.RunQueuedCmd = buffer[iStartIndex];
        iStartIndex += 1;

        this.PauseCmdFlag = buffer[iStartIndex];
        iStartIndex += 1;

        this.VelocityRatio = buffer[iStartIndex];
        iStartIndex += 1;

        this.AccelerationRatio = buffer[iStartIndex];
        iStartIndex += 1;

        this.JerkRatio = buffer[iStartIndex];
        iStartIndex += 1;

        this.XYZVelocityRatio = buffer[iStartIndex];
        iStartIndex += 1;

        this.RVelocityRatio = buffer[iStartIndex];
        iStartIndex += 1;

        this.XYZAccelerationRatio = buffer[iStartIndex];
        iStartIndex += 1;

        this.RAccelerationRatio = buffer[iStartIndex];
        iStartIndex += 1;

        this.XYZJerkRatio = buffer[iStartIndex];
        iStartIndex += 1;

        this.RJerkRatio = buffer[iStartIndex];
        iStartIndex += 1;

        this.BrakeStatus = buffer[iStartIndex];
        iStartIndex += 1;
        this.EnableStatus = buffer[iStartIndex];
        iStartIndex += 1;
        this.DragStatus = buffer[iStartIndex];
        iStartIndex += 1;
        this.RunningStatus = buffer[iStartIndex];
        iStartIndex += 1;
        this.ErrorStatus = buffer[iStartIndex];
        iStartIndex += 1;
        this.JogStatus = buffer[iStartIndex];
        iStartIndex += 1;
        this.RobotType = buffer[iStartIndex];
        iStartIndex += 1;
        this.DragButtonSignal = buffer[iStartIndex];
        iStartIndex += 1;
        this.EnableButtonSignal = buffer[iStartIndex];
        iStartIndex += 1;
        this.RecordButtonSignal = buffer[iStartIndex];
        iStartIndex += 1;
        this.ReappearButtonSignal = buffer[iStartIndex];
        iStartIndex += 1;
        this.JawButtonSignal = buffer[iStartIndex];
        iStartIndex += 1;
        this.SixForceOnline = buffer[iStartIndex];
        iStartIndex += 1;

        for (int i = 0; i < this.Reserved6.length; ++i)
        {
            this.Reserved6[i] = buffer[iStartIndex];
            iStartIndex += 1;
        }

        for (int i = 0; i < this.MActual.length; ++i)
        {
            this.MActual[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        this.Load = BitConverter.toDouble(buffer, iStartIndex);
        iStartIndex += 8;

        this.CenterX = BitConverter.toDouble(buffer, iStartIndex);
        iStartIndex += 8;

        this.CenterY = BitConverter.toDouble(buffer, iStartIndex);
        iStartIndex += 8;

        this.CenterZ = BitConverter.toDouble(buffer, iStartIndex);
        iStartIndex += 8;

        for (int i = 0; i < this.UserValue.length; ++i)
        {
            this.UserValue[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.Tools.length; ++i)
        {
            this.Tools[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        this.TraceIndex = BitConverter.toDouble(buffer, iStartIndex);
        iStartIndex += 8;

        for (int i = 0; i < this.SixForceValue.length; ++i)
        {
            this.SixForceValue[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.TargetQuaternion.length; ++i)
        {
            this.TargetQuaternion[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.ActualQuaternion.length; ++i)
        {
            this.ActualQuaternion[i] = BitConverter.toDouble(buffer, iStartIndex);
            iStartIndex += 8;
        }

        for (int i = 0; i < this.Reserved7.length; ++i)
        {
            this.Reserved7[i] = buffer[iStartIndex];
            iStartIndex += 1;
        }

        this.DataHasRead = true;

    }

    public String convertRobotMode()
    {
        switch (this.RobotMode)
        {
            case -1:
                return "NO_CONTROLLER";
            case 0:
                return "NO_CONNECTED";
            case 1:
                return "ROBOT_MODE_INIT";
            case 2:
                return "ROBOT_MODE_BRAKE_OPEN";
            case 3:
                return "ROBOT_RESERVED";
            case 4:
                return "ROBOT_MODE_DISABLED";
            case 5:
                return "ROBOT_MODE_ENABLE";
            case 6:
                return "ROBOT_MODE_BACKDRIVE";
            case 7:
                return "ROBOT_MODE_RUNNING";
            case 8:
                return "ROBOT_MODE_RECORDING";
            case 9:
                return "ROBOT_MODE_ERROR";
            case 10:
                return "ROBOT_MODE_PAUSE";
            case 11:
                return "ROBOT_MODE_JOG";
        }
        return String.format("UNKNOW：RobotMode="+this.RobotMode+"");
    }
}
