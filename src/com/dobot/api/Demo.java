package com.dobot.api;

import com.dobot.api.entity.JointMovJEntity;
import com.dobot.api.entity.MovJEntity;
import com.dobot.api.entity.MovLEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo extends JFrame{
    JTextField digitalInputsTextField;
    JTextField digitalOutputsTextField;

    JTextField feedbackJ1TextField;
    JTextField feedbackJ2TextField;
    JTextField feedbackJ3TextField;
    JTextField feedbackJ4TextField;
    JTextField feedbackJ5TextField;
    JTextField feedbackJ6TextField;

    JTextField feedbackXTextField;
    JTextField feedbackYTextField;
    JTextField feedbackZTextField;
    JTextField feedbackRxTextField;
    JTextField feedbackRyTextField;
    JTextField feedbackRzTextField;

    JTextField currentSpeedRatioTextField;

    JTextField robotModeTextField;
    public static void main(String[] args) {
        Demo demo = new Demo();
    }
    Dashboard dashboard;
    DobotMove dobotMove;
    Feedback feedback;

    JTextArea  logTextArea = new JTextArea(19,1);

    JTextArea  errorTextArea = new JTextArea(12,1);
    public Demo(){

        logTextArea.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        Logger.instance.addLogListener(logTextArea);

        errorTextArea.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        Logger.instance.addErrorListener(errorTextArea);

        dashboard = new Dashboard();
        dobotMove = new DobotMove();
        feedback = new Feedback();

        this.setTitle("MainForm");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(300,10,1200,900);
        this.setMinimumSize(new Dimension(1200,1000));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        /*****************  Robot Connect 模块界面  ***********/
        JLabel robotConnectTitleLabel;

        JLabel ipAddressLabel;
        JTextField ipAddressTextField;
        JLabel dashboardPortLabel;
        JTextField dashboardPortTextField;
        JLabel movePortLabel;
        JTextField movePortTextField;
        JLabel feedBackPortLabel;
        JTextField feedBackPortTextField;
        JButton connectButton;

        robotConnectTitleLabel = new JLabel("Robot Connect");
        robotConnectTitleLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        robotConnectTitleLabel.setBounds(50,0,1050,30);
        robotConnectTitleLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        ipAddressLabel = new JLabel("IP Address:");
        ipAddressLabel.setBounds(robotConnectTitleLabel.getX()+10,robotConnectTitleLabel.getY()+40,80,50);
        ipAddressLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        ipAddressTextField = new JTextField("192.168.5.1");
        ipAddressTextField.setBounds(ipAddressLabel.getX()+100,ipAddressLabel.getY()+10,120,30);

        dashboardPortLabel = new JLabel("Dashboard Port:");
        dashboardPortLabel.setBounds(ipAddressTextField.getX()+150, ipAddressLabel.getY(),120,50);
        dashboardPortLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        dashboardPortTextField = new JTextField(8);
        dashboardPortTextField.setText("29999");
        dashboardPortTextField.setBounds(dashboardPortLabel.getX()+130, ipAddressLabel.getY()+10,100,30);

        movePortLabel = new JLabel("Move Port:");
        movePortLabel.setBounds(dashboardPortTextField.getX()+150,ipAddressLabel.getY(),100,50);
        movePortLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        movePortTextField = new JTextField(8);
        movePortTextField.setText("30003");
        movePortTextField.setBounds(movePortLabel.getX()+90,ipAddressLabel.getY()+10,100,30);

        feedBackPortLabel = new JLabel("Feedback Port:");
        feedBackPortLabel.setBounds(movePortTextField.getX()+150,ipAddressLabel.getY(),150,50);
        feedBackPortLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        feedBackPortTextField = new JTextField(8);
        feedBackPortTextField.setText("30004");
        feedBackPortTextField.setBounds(feedBackPortLabel.getX()+120,ipAddressLabel.getY()+10,100,30);

        connectButton = new JButton("Connect");
        connectButton.setBounds(feedBackPortTextField.getX(),ipAddressLabel.getY()+60,130,40);
        connectButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        mainPanel.add(robotConnectTitleLabel);
        mainPanel.add(ipAddressTextField);
        mainPanel.add(ipAddressLabel);
        mainPanel.add(dashboardPortLabel);
        mainPanel.add(dashboardPortTextField);
        mainPanel.add(movePortLabel);
        mainPanel.add(movePortTextField);
        mainPanel.add(feedBackPortLabel);
        mainPanel.add(feedBackPortTextField);
        mainPanel.add(connectButton);


        /*************************** Robot Connect 事件监听 ************************/
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = ipAddressTextField.getText();
                if(ip == null || ip.isEmpty()){
                    Logger.instance.log("请输入IP地址");
                    return;
                }
                if(!ipAddressCheck(ip)){
                    Logger.instance.log("IP地址输入格式不正确");
                    return;
                }

                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        if("Disconnect".equals(connectButton.getText())){
                            System.out.println(connectButton.getText());
                            dashboard.disconnect();
                            dobotMove.disconnect();
                            feedback.disconnect();
                            connectButton.setText("Connect");
                            return;
                        }
                        if(!portCheck(dashboardPortTextField.getText())){
                            Logger.instance.log("请输入正确的Dashboard 端口号");
                            return;
                        }
                        if(!portCheck(feedBackPortTextField.getText())){
                            Logger.instance.log("请输入正确的feedback 端口号");
                            return;
                        }
                        if(!portCheck(movePortTextField.getText())){
                            Logger.instance.log("请输入正确的Move 端口号");
                            return;
                        }
                        if(!dashboard.connect(ip,Integer.valueOf(dashboardPortTextField.getText()))){
                            Logger.instance.log("Connect"+ip+"fail");
                            return;
                        }

                        if(!feedback.connect(ipAddressTextField.getText(),Integer.valueOf(feedBackPortTextField.getText()))){
                            Logger.instance.log("Connect"+ip+"fail");
                            return;
                        }

                        if(!dobotMove.connect(ip,Integer.valueOf(movePortTextField.getText()))){
                            Logger.instance.log("Connect"+ip+"fail");
                            return;
                        }
                        Logger.instance.log("Connect Success");
                        connectButton.setText("Disconnect");
                        return;
                    }
                };


                thread.start();

            }

        });


        /********************  Dashboard Function   **********************/
        JLabel dashboardFunctionLabel;

        JButton enableButton;
        JButton resetRobotButton;
        JButton clearErrorButton;
        JLabel speedRatioLabel;
        JTextField percentTextField;
        JLabel percentLabel;
        JButton confirmButton;

        JLabel digitalOutputsLabel;
        JLabel indexlabel;
        JTextField indexTextField;
        JLabel statusLabel;
        JComboBox statusComboBox;
        JButton confirmStatusButton;



        dashboardFunctionLabel = new JLabel("Dashboard Function");
        dashboardFunctionLabel.setBounds(robotConnectTitleLabel.getX(),robotConnectTitleLabel.getY()+150,robotConnectTitleLabel.getWidth(),robotConnectTitleLabel.getHeight());
        dashboardFunctionLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        dashboardFunctionLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        enableButton = new JButton("Enable");
        enableButton.setBounds(dashboardFunctionLabel.getX()+20,dashboardFunctionLabel.getY()+50    ,150,40);
        enableButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        resetRobotButton = new JButton("Reset Robot");
        resetRobotButton.setBounds(enableButton.getX()+200,dashboardFunctionLabel.getY()+50,150,40);
        resetRobotButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));


        clearErrorButton = new JButton("Clear Error");
        clearErrorButton.setBounds(resetRobotButton.getX()+200,dashboardFunctionLabel.getY()+50,150,40);
        clearErrorButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        speedRatioLabel = new JLabel("Speed Ratio:");
        speedRatioLabel.setBounds(clearErrorButton.getX()+200,dashboardFunctionLabel.getY()+50,100,40);
        speedRatioLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        percentTextField = new JTextField(8);
        percentTextField.setBounds(speedRatioLabel.getX()+110,dashboardFunctionLabel.getY()+50,50,40);
        percentTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        percentLabel = new JLabel("%");
        percentLabel.setBounds(percentTextField.getX()+60,dashboardFunctionLabel.getY()+50,50,40);
        percentLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        confirmButton = new JButton("Confirm");
        confirmButton.setBounds(percentLabel.getX()+30,dashboardFunctionLabel.getY()+50,100,40);
        confirmButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        digitalOutputsLabel = new JLabel("Digital Outputs:");
        digitalOutputsLabel.setBounds(enableButton.getX(),enableButton.getY()+60,120,40);
        digitalOutputsLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        indexlabel = new JLabel("Index:");
        indexlabel.setBounds(digitalOutputsLabel.getX()+140,enableButton.getY()+60,60,40);
        indexlabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        indexTextField = new JTextField(6);
        indexTextField.setBounds(indexlabel.getX()+50,enableButton.getY()+60,60,40);
        indexTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        statusLabel = new JLabel("Status:");
        statusLabel.setBounds(indexTextField.getX()+80,enableButton.getY()+60,60,40);
        statusLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        statusComboBox = new JComboBox();
        statusComboBox.addItem("On");
        statusComboBox.addItem("Off");
        statusComboBox.setBounds(statusLabel.getX()+60,enableButton.getY()+60,60,40);
        statusComboBox.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        confirmStatusButton = new JButton("Confirm");
        confirmStatusButton.setBounds(statusComboBox.getX()+80,enableButton.getY()+60,100,40);
        confirmStatusButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));


        mainPanel.add(dashboardFunctionLabel);
        mainPanel.add(enableButton);
        mainPanel.add(resetRobotButton);
        mainPanel.add(clearErrorButton);
        mainPanel.add(speedRatioLabel);
        mainPanel.add(percentTextField);
        mainPanel.add(percentLabel);
        mainPanel.add(confirmButton);

        mainPanel.add(digitalOutputsLabel);
        mainPanel.add(indexlabel);
        mainPanel.add(indexTextField);
        mainPanel.add(statusLabel);
        mainPanel.add(statusComboBox);
        mainPanel.add(confirmStatusButton);


        /****************************  Dashboard 事件监听  ***************************************/

        enableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if("Enable".equals(e.getActionCommand())){
                    Thread thread = new Thread(){
                        @Override
                        public void run(){
                            String reply = dashboard.enableRobot();
                            return;
                        }
                    };
                    thread.start();
                    enableButton.setText("Disable");
                }else{
                    Thread thread = new Thread(){
                        @Override
                        public void run(){
                            String reply = dashboard.disableRobot();
                            return;
                        }
                    };
                    thread.start();
                    enableButton.setText("Enable");
                }

            }
        });

        resetRobotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(){
                    @Override
                    public void run(){
                        String reply = dashboard.resetRobot();
                        return;
                    }
                };
                thread.start();
            }
        });

        clearErrorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(){
                    @Override
                    public void run(){
                        String reply = dashboard.clearError();
                        return;
                    }
                };
                thread.start();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(){
                    @Override
                    public void run(){
                        String speedPercent = percentTextField.getText();
                        if(speedPercent == null || speedPercent.isEmpty()){
                            Logger.instance.log("请输入速度比例");
                            return;
                        }
                        Integer speed = Integer.valueOf(speedPercent);
                        if(speed < 1||speed > 100){
                            Logger.instance.log("请输入正确的速度比例");
                            return;
                        }
                        String reply = dashboard.speedFactor(speed);
                    }
                };
                thread.start();
            }
        });

        confirmStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(){
                    @Override
                    public void run(){
                        String doIndex = indexTextField.getText();
                        if(doIndex == null || doIndex.isEmpty()){
                            Logger.instance.log("请输入数字输出索引");
                            return;
                        }
                        Integer doIndexInt = Integer.valueOf(doIndex);
                        if((doIndexInt >= 1 && doIndexInt <= 16)||(doIndexInt >= 100 && doIndexInt <= 1000)){
                            String str = String.valueOf(statusComboBox.getSelectedItem());
                            if(str.equals("On")){
                                String reply = dashboard.DigitalOutputs(doIndexInt,true);
                            }else {
                                String reply = dashboard.DigitalOutputs(doIndexInt,false);
                            }
                        }else {
                            Logger.instance.log("请输入正确的数字输出索引");
                        }
                        return;

                    }
                };
                thread.start();
            }
        });


        /********************  Move Function   **********************/
        JLabel moveFunctionLabel;

        JLabel XLabel;
        JTextField XTextField;
        JLabel YLabel;
        JTextField YTextField;
        JLabel ZLabel;
        JTextField ZTextField;
        JLabel RxLabel;
        JTextField RxTextField;
        JLabel RyLabel;
        JTextField RyTextField;
        JLabel RzLabel;
        JTextField RzTextField;

        JButton movJButton;
        JButton movLButton;

        JLabel J1Label;
        JTextField J1TextField;
        JLabel J2Label;
        JTextField J2TextField;
        JLabel J3Label;
        JTextField J3TextField;
        JLabel J4Label;
        JTextField J4TextField;
        JLabel J5Label;
        JTextField J5TextField;
        JLabel J6Label;
        JTextField J6TextField;

        JButton JointMovJButton;

        moveFunctionLabel = new JLabel("Move Function");
        moveFunctionLabel.setBounds(robotConnectTitleLabel.getX(),dashboardFunctionLabel.getY()+170,robotConnectTitleLabel.getWidth(),robotConnectTitleLabel.getHeight());
        moveFunctionLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        moveFunctionLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        XLabel = new JLabel("X:");
        XLabel.setBounds(moveFunctionLabel.getX()+30,moveFunctionLabel.getY()+50,80,40);
        XLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        XTextField = new JTextField(8);
        XTextField.setBounds(XLabel.getX()+30,moveFunctionLabel.getY()+40,50,40);
        XTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        YLabel = new JLabel("Y:");
        YLabel.setBounds(XTextField.getX()+80,moveFunctionLabel.getY()+40,80,40);
        YLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        YTextField = new JTextField(8);
        YTextField.setBounds(YLabel.getX()+30,moveFunctionLabel.getY()+40,50,40);
        YTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        ZLabel = new JLabel("Z:");
        ZLabel.setBounds(YTextField.getX()+80,moveFunctionLabel.getY()+40,80,40);
        ZLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        ZTextField = new JTextField(8);
        ZTextField.setBounds(ZLabel.getX()+30,moveFunctionLabel.getY()+40,50,40);
        ZTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        RxLabel = new JLabel("Rx:");
        RxLabel.setBounds(ZTextField.getX()+80,moveFunctionLabel.getY()+40,80,40);
        RxLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        RxTextField = new JTextField(8);
        RxTextField.setBounds(RxLabel.getX()+30,moveFunctionLabel.getY()+40,50,40);
        RxTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        RyLabel = new JLabel("Ry:");
        RyLabel.setBounds(RxTextField.getX()+80,moveFunctionLabel.getY()+40,80,40);
        RyLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        RyTextField = new JTextField(8);
        RyTextField.setBounds(RyLabel.getX()+30,moveFunctionLabel.getY()+40,50,40);
        RyTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        RzLabel = new JLabel("Rz:");
        RzLabel.setBounds(RyTextField.getX()+80,moveFunctionLabel.getY()+40,80,40);
        RzLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        RzTextField = new JTextField(8);
        RzTextField.setBounds(RzLabel.getX()+30,moveFunctionLabel.getY()+40,50,40);
        RzTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        movJButton = new JButton("MovJ");
        movJButton.setBounds(RzTextField.getX()+100,moveFunctionLabel.getY()+40,100,40);
        movJButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        movLButton = new JButton("MovL");
        movLButton.setBounds(movJButton.getX()+120,moveFunctionLabel.getY()+40,100,40);
        movLButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        J1Label = new JLabel("J1:");
        J1Label.setBounds(moveFunctionLabel.getX()+30,moveFunctionLabel.getY()+110,100,40);
        J1Label.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        J1TextField = new JTextField(8);
        J1TextField.setBounds(J1Label.getX()+30,moveFunctionLabel.getY()+110,50,40);
        J1TextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        J2Label = new JLabel("J2:");
        J2Label.setBounds(J1TextField.getX()+80,moveFunctionLabel.getY()+110,100,40);
        J2Label.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        J2TextField = new JTextField(8);
        J2TextField.setBounds(J2Label.getX()+30,moveFunctionLabel.getY()+110,50,40);
        J2TextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        J3Label = new JLabel("J3:");
        J3Label.setBounds(J2TextField.getX()+80,moveFunctionLabel.getY()+110,100,40);
        J3Label.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        J3TextField = new JTextField(8);
        J3TextField.setBounds(J3Label.getX()+30,moveFunctionLabel.getY()+110,50,40);
        J3TextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        J4Label = new JLabel("J4:");
        J4Label.setBounds(J3TextField.getX()+80,moveFunctionLabel.getY()+110,100,40);
        J4Label.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        J4TextField = new JTextField(8);
        J4TextField.setBounds(J4Label.getX()+30,moveFunctionLabel.getY()+110,50,40);
        J4TextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        J5Label = new JLabel("J5:");
        J5Label.setBounds(J4TextField.getX()+80,moveFunctionLabel.getY()+110,100,40);
        J5Label.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        J5TextField = new JTextField(8);
        J5TextField.setBounds(J5Label.getX()+30,moveFunctionLabel.getY()+110,50,40);
        J5TextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        J6Label = new JLabel("J6:");
        J6Label.setBounds(J5TextField.getX()+80,moveFunctionLabel.getY()+110,100,40);
        J6Label.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        J6TextField = new JTextField(8);
        J6TextField.setBounds(J6Label.getX()+30,moveFunctionLabel.getY()+110,50,40);
        J6TextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));


        JointMovJButton = new JButton("JointMovJ");
        JointMovJButton.setBounds(J6TextField.getX()+100,moveFunctionLabel.getY()+110,120,40);
        JointMovJButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));


        mainPanel.add(moveFunctionLabel);
        mainPanel.add(XLabel);
        mainPanel.add(XTextField);
        mainPanel.add(YLabel);
        mainPanel.add(YTextField);
        mainPanel.add(ZLabel);
        mainPanel.add(ZTextField);
        mainPanel.add(RxLabel);
        mainPanel.add(RxTextField);
        mainPanel.add(RyLabel);
        mainPanel.add(RyTextField);
        mainPanel.add(RzLabel);
        mainPanel.add(RzTextField);
        mainPanel.add(movJButton);
        mainPanel.add(movLButton);
        mainPanel.add(J1Label);
        mainPanel.add(J1TextField);
        mainPanel.add(J2Label);
        mainPanel.add(J2TextField);
        mainPanel.add(J3Label);
        mainPanel.add(J3TextField);
        mainPanel.add(J4Label);
        mainPanel.add(J4TextField);
        mainPanel.add(J5Label);
        mainPanel.add(J5TextField);
        mainPanel.add(J6Label);
        mainPanel.add(J6TextField);
        mainPanel.add(JointMovJButton);


        movJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(){
                    @Override
                    public void run(){
                       boolean flag = isDataTrue(XTextField.getText(),YTextField.getText(),ZTextField.getText(),RxTextField.getText(),RyTextField.getText(),RzTextField.getText());
                       if(!flag){
                           Logger.instance.log("请输入正确的MovJ数据");

                       }
                        MovJEntity movJEntity = new MovJEntity();
                        movJEntity.X = Integer.valueOf(XTextField.getText());
                        movJEntity.Y = Integer.valueOf(YTextField.getText());
                        movJEntity.Z = Integer.valueOf(ZTextField.getText());
                        movJEntity.Rx = Integer.valueOf(RxTextField.getText());
                        movJEntity.Ry = Integer.valueOf(RyTextField.getText());
                        movJEntity.Rz = Integer.valueOf(RzTextField.getText());
                        String reply = dobotMove.MovJ(movJEntity);
                        return;
                    }

                };
                thread.start();
            }
        });

        movLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(){
                    @Override
                    public void run(){
                        boolean flag = isDataTrue(XTextField.getText(),YTextField.getText(),ZTextField.getText(),RxTextField.getText(),RyTextField.getText(),RzTextField.getText());
                        if(!flag){
                            Logger.instance.log("请输入正确的MovL数据");
                            return;
                        }
                        MovLEntity movLEntity = new MovLEntity();
                        movLEntity.X = Integer.valueOf(XTextField.getText());
                        movLEntity.Y = Integer.valueOf(YTextField.getText());
                        movLEntity.Z = Integer.valueOf(ZTextField.getText());
                        movLEntity.Rx = Integer.valueOf(RxTextField.getText());
                        movLEntity.Ry = Integer.valueOf(RyTextField.getText());
                        movLEntity.Rz = Integer.valueOf(RzTextField.getText());
                        String reply = dobotMove.MovL(movLEntity);
                        return;
                    }

                };
                thread.start();
            }
        });


        JointMovJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(){
                    @Override
                    public void run(){
                        boolean flag = isDataTrue(J1TextField.getText(),J2TextField.getText(),J3TextField.getText(),J4TextField.getText(),J5TextField.getText(),J6TextField.getText());
                        if(!flag){
                            Logger.instance.log("请输入正确的JointMovJ数据");
                            return;
                        }
                        JointMovJEntity jointMovJEntity = new JointMovJEntity();
                        jointMovJEntity.J1 = Integer.valueOf(J1TextField.getText());
                        jointMovJEntity.J2 = Integer.valueOf(J2TextField.getText());
                        jointMovJEntity.J3 = Integer.valueOf(J3TextField.getText());
                        jointMovJEntity.J4 = Integer.valueOf(J4TextField.getText());
                        jointMovJEntity.J5 = Integer.valueOf(J5TextField.getText());
                        jointMovJEntity.J6 = Integer.valueOf(J6TextField.getText());
                        String reply = dobotMove.JointMovJ(jointMovJEntity);
                        return;
                    }
                };
                thread.start();
            }
        });


        /*************************  Feedback  **************************/
        JLabel feedbackLabel;
        JLabel logLabel;
        JLabel errorInfoLabel;

        feedbackLabel = new JLabel("Feedback");
        feedbackLabel.setBounds(robotConnectTitleLabel.getX(),dashboardFunctionLabel.getY()+350,700,robotConnectTitleLabel.getHeight());
        feedbackLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        feedbackLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        logLabel = new JLabel("Log");
        logLabel.setBounds(robotConnectTitleLabel.getX()+710,dashboardFunctionLabel.getY()+350,340,robotConnectTitleLabel.getHeight());
        logLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        logLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        errorInfoLabel = new JLabel("Error Info");
        errorInfoLabel.setBounds(robotConnectTitleLabel.getX()+450,feedbackLabel.getY()+40,250,20);
        errorInfoLabel.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        errorInfoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        logTextArea.setBounds(logLabel.getX(),logLabel.getY()+40,340,400);
        JScrollPane logScrollPane = new JScrollPane();
        logScrollPane.setBounds(logLabel.getX(),logLabel.getY()+40,340,400);
        logScrollPane.setViewportView(logTextArea);

        errorTextArea.setBounds(errorInfoLabel.getX(),errorInfoLabel.getY()+30,250,300);
        JScrollPane errorScrollPane = new JScrollPane();
        errorScrollPane.setBounds(errorInfoLabel.getX(),errorInfoLabel.getY()+30,250,300);
        errorScrollPane.setViewportView(errorTextArea);

        mainPanel.add(feedbackLabel);
        mainPanel.add(logLabel);
        mainPanel.add(errorInfoLabel);
        mainPanel.add(logScrollPane);
        mainPanel.add(errorScrollPane);



        /*********************************  Feedback 左下角模块  ****************************************/
        JButton add1Button;
        JButton add2Button;
        JButton add3Button;
        JButton add4Button;
        JButton add5Button;
        JButton add6Button;

        JButton minus1Button;
        JButton minus2Button;
        JButton minus3Button;
        JButton minus4Button;
        JButton minus5Button;
        JButton minus6Button;

        JButton addXButton;
        JButton addYButton;
        JButton addZButton;
        JButton addRxButton;
        JButton addRyButton;
        JButton addRzButton;

        JButton minusXButton;
        JButton minusYButton;
        JButton minusZButton;
        JButton minusRxButton;
        JButton minusRyButton;
        JButton minusRzButton;


        minus1Button = new JButton("J1-");
        FeedbackButtonListeners minus1ButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        minus1Button.addActionListener(minus1ButtonFeedbackButtonListeners);
        minus1Button.addMouseListener(minus1ButtonFeedbackButtonListeners);
        minus1Button.setBounds(feedbackLabel.getX(),feedbackLabel.getY()+100,40,30);
        minus1Button.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        minus1Button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        minus2Button = new JButton("J2-");
        FeedbackButtonListeners minus2ButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        minus2Button.addActionListener(minus2ButtonFeedbackButtonListeners);
        minus2Button.addMouseListener(minus2ButtonFeedbackButtonListeners);


        minus2Button.addActionListener(new FeedbackButtonListeners());
        minus2Button.setBounds(feedbackLabel.getX(),minus1Button.getY()+50,40,30);
        minus2Button.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        minus2Button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        minus3Button = new JButton("J3-");
        FeedbackButtonListeners minus3ButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        minus3Button.addActionListener(minus3ButtonFeedbackButtonListeners);
        minus3Button.addMouseListener(minus3ButtonFeedbackButtonListeners);

        minus3Button.setBounds(feedbackLabel.getX(),minus2Button.getY()+50,40,30);
        minus3Button.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        minus3Button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        minus4Button = new JButton("J4-");

        FeedbackButtonListeners minus4ButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        minus4Button.addActionListener(minus4ButtonFeedbackButtonListeners);
        minus4Button.addMouseListener(minus4ButtonFeedbackButtonListeners);

        minus4Button.setBounds(feedbackLabel.getX(),minus3Button.getY()+50,40,30);
        minus4Button.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        minus4Button.setBorder(BorderFactory.createLineBorder(Color.BLACK));


        minus5Button = new JButton("J5-");

        FeedbackButtonListeners minus5ButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        minus5Button.addActionListener(minus5ButtonFeedbackButtonListeners);
        minus5Button.addMouseListener(minus5ButtonFeedbackButtonListeners);
        minus5Button.setBounds(feedbackLabel.getX(),minus4Button.getY()+50,40,30);
        minus5Button.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        minus5Button.setBorder(BorderFactory.createLineBorder(Color.BLACK));


        minus6Button = new JButton("J6-");
        FeedbackButtonListeners minus6ButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        minus6Button.addActionListener(minus6ButtonFeedbackButtonListeners);
        minus6Button.addMouseListener(minus6ButtonFeedbackButtonListeners);
        minus6Button.setBounds(feedbackLabel.getX(),minus5Button.getY()+50,40,30);
        minus6Button.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        minus6Button.setBorder(BorderFactory.createLineBorder(Color.BLACK));


        add1Button = new JButton("J1+");
        FeedbackButtonListeners add1ButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        add1Button.addActionListener(add1ButtonFeedbackButtonListeners);
        add1Button.addMouseListener(add1ButtonFeedbackButtonListeners);
        add1Button.setBounds(feedbackLabel.getX()+190,feedbackLabel.getY()+100,40,30);
        add1Button.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        add1Button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        add2Button  = new JButton("J2+");
        FeedbackButtonListeners add2ButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        add2Button.addActionListener(add2ButtonFeedbackButtonListeners);
        add2Button.addMouseListener(add2ButtonFeedbackButtonListeners);
        add2Button.addActionListener(new FeedbackButtonListeners());
        add2Button.setBounds(feedbackLabel.getX()+190,add1Button.getY()+50,40,30);
        add2Button.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        add2Button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        add3Button  = new JButton("J3+");
        FeedbackButtonListeners add3ButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        add3Button.addActionListener(add3ButtonFeedbackButtonListeners);
        add3Button.addMouseListener(add3ButtonFeedbackButtonListeners);
        add3Button.setBounds(feedbackLabel.getX()+190,add2Button.getY()+50,40,30);
        add3Button.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        add3Button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        add4Button  = new JButton("J4+");
        FeedbackButtonListeners add4ButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        add4Button.addActionListener(add4ButtonFeedbackButtonListeners);
        add4Button.addMouseListener(add4ButtonFeedbackButtonListeners);
        add4Button.setBounds(feedbackLabel.getX()+190,add3Button.getY()+50,40,30);
        add4Button.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        add4Button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        add5Button  = new JButton("J5+");
        FeedbackButtonListeners add5ButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        add5Button.addActionListener(add5ButtonFeedbackButtonListeners);
        add5Button.addMouseListener(add5ButtonFeedbackButtonListeners);
        add5Button.setBounds(feedbackLabel.getX()+190,add4Button.getY()+50,40,30);
        add5Button.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        add5Button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        add6Button  = new JButton("J6+");
        FeedbackButtonListeners add6ButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        add6Button.addActionListener(add6ButtonFeedbackButtonListeners);
        add6Button.addMouseListener(add6ButtonFeedbackButtonListeners);
        add6Button.setBounds(feedbackLabel.getX()+190,add5Button.getY()+50,40,30);
        add6Button.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        add6Button.setBorder(BorderFactory.createLineBorder(Color.BLACK));


        minusXButton  = new JButton("X-");
        FeedbackButtonListeners minusXButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        minusXButton.addActionListener(minusXButtonFeedbackButtonListeners);
        minusXButton.addMouseListener(minusXButtonFeedbackButtonListeners);
        minusXButton.setBounds(feedbackLabel.getX()+240,feedbackLabel.getY()+100,40,30);
        minusXButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        minusXButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        minusYButton  = new JButton("Y-");
        FeedbackButtonListeners minusYButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        minusYButton.addActionListener(minusYButtonFeedbackButtonListeners);
        minusYButton.addMouseListener(minusYButtonFeedbackButtonListeners);
        minusYButton.setBounds(feedbackLabel.getX()+240,minusXButton.getY()+50,40,30);
        minusYButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        minusYButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        minusZButton  = new JButton("Z-");
        FeedbackButtonListeners minusZButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        minusZButton.addActionListener(minusZButtonFeedbackButtonListeners);
        minusZButton.addMouseListener(minusZButtonFeedbackButtonListeners);
        minusZButton.addActionListener(new FeedbackButtonListeners());
        minusZButton.setBounds(feedbackLabel.getX()+240,minusYButton.getY()+50,40,30);
        minusZButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        minusZButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        minusRxButton  = new JButton("Rx-");
        FeedbackButtonListeners minusRxButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        minusRxButton.addActionListener(minusRxButtonFeedbackButtonListeners);
        minusRxButton.addMouseListener(minusRxButtonFeedbackButtonListeners);
        minusRxButton.setBounds(feedbackLabel.getX()+240,minusZButton.getY()+50,40,30);
        minusRxButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        minusRxButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        minusRyButton  = new JButton("Ry-");
        FeedbackButtonListeners minusRyButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        minusRyButton.addActionListener(minusRyButtonFeedbackButtonListeners);
        minusRyButton.addMouseListener(minusRyButtonFeedbackButtonListeners);
        minusRyButton.setBounds(feedbackLabel.getX()+240,minusRxButton.getY()+50,40,30);
        minusRyButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        minusRyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        minusRzButton  = new JButton("Rz-");
        FeedbackButtonListeners minusRzButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        minusRzButton.addActionListener(minusRzButtonFeedbackButtonListeners);
        minusRzButton.addMouseListener(minusRzButtonFeedbackButtonListeners);
        minusRzButton.setBounds(feedbackLabel.getX()+240,minusRyButton.getY()+50,40,30);
        minusRzButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        minusRzButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addXButton  = new JButton("X+");
        FeedbackButtonListeners addXButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        addXButton.addActionListener(addXButtonFeedbackButtonListeners);
        addXButton.addMouseListener(addXButtonFeedbackButtonListeners);
        addXButton.setBounds(feedbackLabel.getX()+400,feedbackLabel.getY()+100,40,30);
        addXButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        addXButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addYButton  = new JButton("Y+");
        FeedbackButtonListeners addYButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        addYButton.addActionListener(addYButtonFeedbackButtonListeners);
        addYButton.addMouseListener(addYButtonFeedbackButtonListeners);
        addYButton.setBounds(feedbackLabel.getX()+400,addXButton.getY()+50,40,30);
        addYButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        addYButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addZButton = new JButton("Z+");
        FeedbackButtonListeners addZButtonFeedbackButtonListeners = new FeedbackButtonListeners();
        addZButton.addActionListener(addZButtonFeedbackButtonListeners);
        addZButton.addMouseListener(addZButtonFeedbackButtonListeners);
        addZButton.setBounds(feedbackLabel.getX()+400,addYButton.getY()+50,40,30);
        addZButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        addZButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addRxButton = new JButton("Rx+");
        FeedbackButtonListeners addRxButtonbackButtonListeners = new FeedbackButtonListeners();
        addRxButton.addActionListener(addRxButtonbackButtonListeners);
        addRxButton.addMouseListener(addRxButtonbackButtonListeners);
        addRxButton.setBounds(feedbackLabel.getX()+400,addZButton.getY()+50,40,30);
        addRxButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        addRxButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addRyButton = new JButton("Ry+");
        FeedbackButtonListeners addRyButtonListeners = new FeedbackButtonListeners();
        addRyButton.addActionListener(addRyButtonListeners);
        addRyButton.addMouseListener(addRyButtonListeners);
        addRyButton.setBounds(feedbackLabel.getX()+400,addRxButton.getY()+50,40,30);
        addRyButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        addRyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addRzButton = new JButton("Rz+");
        FeedbackButtonListeners addRzButtonListeners = new FeedbackButtonListeners();
        addRzButton.addActionListener(addRzButtonListeners);
        addRzButton.addMouseListener(addRzButtonListeners);
        addRzButton.setBounds(feedbackLabel.getX()+400,addRyButton.getY()+50,40,30);
        addRzButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        addRzButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        mainPanel.add(minus1Button);
        mainPanel.add(minus2Button);
        mainPanel.add(minus3Button);
        mainPanel.add(minus4Button);
        mainPanel.add(minus5Button);
        mainPanel.add(minus6Button);

        mainPanel.add(add1Button);
        mainPanel.add(add2Button);
        mainPanel.add(add3Button);
        mainPanel.add(add4Button);
        mainPanel.add(add5Button);
        mainPanel.add(add6Button);

        mainPanel.add(minusXButton);
        mainPanel.add(minusYButton);
        mainPanel.add(minusZButton);
        mainPanel.add(minusRxButton);
        mainPanel.add(minusRyButton);
        mainPanel.add(minusRzButton);

        mainPanel.add(addXButton);
        mainPanel.add(addYButton);
        mainPanel.add(addZButton);
        mainPanel.add(addRxButton);
        mainPanel.add(addRyButton);
        mainPanel.add(addRzButton);



        feedbackJ1TextField = new JTextField("J1:"+feedback.QActual[0]);
        feedbackJ2TextField = new JTextField("J2:"+feedback.QActual[1]);
        feedbackJ3TextField = new JTextField("J3:"+feedback.QActual[2]);
        feedbackJ4TextField = new JTextField("J4:"+feedback.QActual[3]);
        feedbackJ5TextField = new JTextField("J5:"+feedback.QActual[4]);
        feedbackJ6TextField = new JTextField("J6:"+feedback.QActual[5]);

        feedbackXTextField = new JTextField("X:" + feedback.ToolVectorActual[0]);
        feedbackYTextField = new JTextField("Y:" + feedback.ToolVectorActual[1]);
        feedbackZTextField = new JTextField("Z:" + feedback.ToolVectorActual[2]);
        feedbackRxTextField = new JTextField("Rx:" + feedback.ToolVectorActual[3]);
        feedbackRyTextField = new JTextField("Ry:" + feedback.ToolVectorActual[4]);
        feedbackRzTextField = new JTextField("Rz:" + feedback.ToolVectorActual[5]);

        feedbackJ1TextField.setBounds(feedbackLabel.getX()+70,feedbackLabel.getY()+100,80,30);
        feedbackJ1TextField.setEditable(false);
        feedbackJ1TextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        feedbackJ1TextField.setBorder(BorderFactory.createEmptyBorder());

        feedbackJ2TextField.setBounds(feedbackLabel.getX()+70,minus1Button.getY()+50,80,30);
        feedbackJ2TextField.setEditable(false);
        feedbackJ2TextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        feedbackJ2TextField.setBorder(BorderFactory.createEmptyBorder());

        feedbackJ3TextField.setBounds(feedbackLabel.getX()+70,minus2Button.getY()+50,80,30);
        feedbackJ3TextField.setEditable(false);
        feedbackJ3TextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        feedbackJ3TextField.setBorder(BorderFactory.createEmptyBorder());


        feedbackJ4TextField.setBounds(feedbackLabel.getX()+70,minus3Button.getY()+50,80,30);
        feedbackJ4TextField.setEditable(false);
        feedbackJ4TextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        feedbackJ4TextField.setBorder(BorderFactory.createEmptyBorder());

        feedbackJ5TextField.setBounds(feedbackLabel.getX()+70,minus4Button.getY()+50,80,30);
        feedbackJ5TextField.setEditable(false);
        feedbackJ5TextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        feedbackJ5TextField.setBorder(BorderFactory.createEmptyBorder());

        feedbackJ6TextField.setBounds(feedbackLabel.getX()+70,minus5Button.getY()+50,80,30);
        feedbackJ6TextField.setEditable(false);
        feedbackJ6TextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        feedbackJ6TextField.setBorder(BorderFactory.createEmptyBorder());


        feedbackXTextField.setBounds(feedbackLabel.getX()+310,feedbackLabel.getY()+100,80,30);
        feedbackXTextField.setEditable(false);
        feedbackXTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        feedbackXTextField.setBorder(BorderFactory.createEmptyBorder());

        feedbackYTextField.setBounds(feedbackLabel.getX()+310,feedbackXTextField.getY()+50,80,30);
        feedbackYTextField.setEditable(false);
        feedbackYTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        feedbackYTextField.setBorder(BorderFactory.createEmptyBorder());

        feedbackZTextField.setBounds(feedbackLabel.getX()+310,feedbackYTextField.getY()+50,80,30);
        feedbackZTextField.setEditable(false);
        feedbackZTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        feedbackZTextField.setBorder(BorderFactory.createEmptyBorder());

        feedbackRxTextField.setBounds(feedbackLabel.getX()+310,feedbackZTextField.getY()+50,80,30);
        feedbackRxTextField.setEditable(false);
        feedbackRxTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        feedbackRxTextField.setBorder(BorderFactory.createEmptyBorder());

        feedbackRyTextField.setBounds(feedbackLabel.getX()+310,feedbackRxTextField.getY()+50,80,30);
        feedbackRyTextField.setEditable(false);
        feedbackRyTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        feedbackRyTextField.setBorder(BorderFactory.createEmptyBorder());

        feedbackRzTextField.setBounds(feedbackLabel.getX()+310,feedbackRyTextField.getY()+50,80,30);
        feedbackRzTextField.setEditable(false);
        feedbackRzTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        feedbackRzTextField.setBorder(BorderFactory.createEmptyBorder());


        mainPanel.add(feedbackJ1TextField);
        mainPanel.add(feedbackJ2TextField);
        mainPanel.add(feedbackJ3TextField);
        mainPanel.add(feedbackJ4TextField);
        mainPanel.add(feedbackJ5TextField);
        mainPanel.add(feedbackJ6TextField);

        mainPanel.add(feedbackXTextField);
        mainPanel.add(feedbackYTextField);
        mainPanel.add(feedbackZTextField);
        mainPanel.add(feedbackRxTextField);
        mainPanel.add(feedbackRyTextField);
        mainPanel.add(feedbackRzTextField);


        currentSpeedRatioTextField = new JTextField("Current Speed Ratio: "+feedback.SpeedScaling+"%");
        currentSpeedRatioTextField.setBounds(feedbackLabel.getX(),feedbackLabel.getY()+30,350,30);
        currentSpeedRatioTextField.setEditable(false);
        currentSpeedRatioTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        currentSpeedRatioTextField.setBorder(BorderFactory.createEmptyBorder());


        robotModeTextField = new JTextField("Robot Mode: "+feedback.convertRobotMode());
        robotModeTextField.setBounds(feedbackLabel.getX(),feedbackLabel.getY()+60,350,30);
        robotModeTextField.setEditable(false);
        robotModeTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        robotModeTextField.setBorder(BorderFactory.createEmptyBorder());

        mainPanel.add(currentSpeedRatioTextField);
        mainPanel.add(robotModeTextField);


        digitalInputsTextField = new JTextField("Digital Inputs:"+feedback.DigitalInputs);
        digitalInputsTextField.setBounds(feedbackLabel.getX(),feedbackLabel.getY()+390,610,30);
        digitalInputsTextField.setEditable(false);
        digitalInputsTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        digitalInputsTextField.setBorder(BorderFactory.createEmptyBorder());

        digitalOutputsTextField = new JTextField("Digital Outputs:"+feedback.DigitalOutputs);
        digitalOutputsTextField.setBounds(feedbackLabel.getX(),feedbackLabel.getY()+420,610,30);
        digitalOutputsTextField.setEditable(false);
        digitalOutputsTextField.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        digitalOutputsTextField.setBorder(BorderFactory.createEmptyBorder());

        mainPanel.add(digitalInputsTextField);
        mainPanel.add(digitalOutputsTextField);

        Thread whileRunThread = new Thread(){
          @Override
          public void run(){
              while(true){
                  try {
                      DecimalFormat df = new DecimalFormat("######0.00");
                      feedbackJ1TextField.setText("J1:"+ df.format(feedback.QActual[0]));
                      feedbackJ2TextField.setText("J2:"+ df.format(feedback.QActual[1]));
                      feedbackJ3TextField.setText("J3:"+ df.format(feedback.QActual[2]));
                      feedbackJ4TextField.setText("J4:"+ df.format(feedback.QActual[3]));
                      feedbackJ5TextField.setText("J5:"+ df.format(feedback.QActual[4]));
                      feedbackJ6TextField.setText("J6:"+ df.format(feedback.QActual[5]));
                      feedbackXTextField.setText("X:" + df.format(feedback.ToolVectorActual[0]));
                      feedbackYTextField.setText("Y:" + df.format(feedback.ToolVectorActual[1]));
                      feedbackZTextField.setText("Z:" + df.format(feedback.ToolVectorActual[2]));
                      feedbackRxTextField.setText("Rx:" + df.format(feedback.ToolVectorActual[3]));
                      feedbackRyTextField.setText("Ry:" + df.format(feedback.ToolVectorActual[4]));
                      feedbackRzTextField.setText("Rz:" + df.format(feedback.ToolVectorActual[5]));
                      currentSpeedRatioTextField.setText("Current Speed Ratio: "+feedback.SpeedScaling +"%");
                      robotModeTextField.setText("Robot Mode:"+feedback.convertRobotMode());
                      digitalInputsTextField.setText("Digital Inputs:"+Long.toBinaryString(feedback.DigitalInputs));
                      digitalOutputsTextField.setText("Digital Outputs:"+Long.toBinaryString(feedback.DigitalOutputs));

                      Thread.sleep(100);
                  } catch (InterruptedException e) {
                      Logger.instance.log(e.getMessage());
                      return;

                  }
              }
          }
        };
        whileRunThread.start();

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(feedbackLabel.getX()+620,feedbackLabel.getY()+390,80,30);
        clearButton.setFont(new Font (Font.DIALOG, Font.BOLD, 15));

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorTextArea.setText("");
            }
        });

        mainPanel.add(clearButton);

        this.add(mainPanel);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if("Disconnect".equals(connectButton.getText())) {
                    super.windowClosing(e);
                    dashboard.disconnect();
                    dobotMove.disconnect();
                    feedback.disconnect();
                }
                System.exit(1);
            }
        });

    }



    public boolean ipAddressCheck(String ip){
        String patternString = "(([0,1]?\\d?\\d|2[0-4]\\d|25[0-5])\\.){3}([0,1]?\\d?\\d|2[0-4]\\d|25[0-5])";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    public boolean portCheck(String port) {
        if(port == null || port.isEmpty()){
            return false;
        }
        Integer iPort = Integer.valueOf(port);
        if(iPort<1||iPort>65535){
            return false;
        }
        return true;
    }


    public boolean isDataTrue(String J1X,String J2Y,String J3Z,String J4Rx,String J5Ry,String J6Rz){
        if(!isStringNotNullAndEmpty(J1X)) {
            System.out.println("请输入正确的X值或J1值");
            return false;
        }
        if(!isStringNotNullAndEmpty(J2Y)){
            System.out.println("请输入正确的Y值或J2值");
            return false;
        }
        if(!isStringNotNullAndEmpty(J3Z)) {
            System.out.println("请输入正确的Z值或J3值");
            return false;
        }
        if(!isStringNotNullAndEmpty(J4Rx)) {
            System.out.println("请输入正确的Rx值或J4值");
            return false;
        }
        if(!isStringNotNullAndEmpty(J5Ry)) {
            System.out.println("请输入正确的Ry值或J5值");
            return false;
        }
        if(!isStringNotNullAndEmpty(J6Rz)) {
            System.out.println("请输入正确的Rz值或J6值");
            return false;
        }
        return true;
    }

    public boolean isStringNotNullAndEmpty(String str){

        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");

        if(str == null || str.isEmpty()|| !pattern.matcher(str).matches()){
            return false;
        }
        return true;
    }

    class FeedbackButtonListeners implements MouseListener,ActionListener{
        String buttonName = null;
        @Override
        public void actionPerformed(ActionEvent e) {
            buttonName = e.getActionCommand();

        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            AbstractButton EventSource = (AbstractButton)e.getSource();
            dobotMove.moveJog(EventSource.getText());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            dobotMove.moveJog(null);

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }


}
