using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Net.Sockets;
using System.Net;
using System.Threading;

namespace RemoteMouseComputer
{
    //主控制类
    class FormStartControl
    {
        private NotifyIcon notifyIcon;//托盘控件的引用

        private EndPoint mobileEp;//EndPoint可以保存发来的手机端的ip和端口,直接用于socket发送的参数
        private IPEndPoint localIp;//电脑本地的ip和监听端口
        private Socket mouseCtrlSocket;//用于接收客户端控制指令的全局socket

        private Thread boardCastThread;//搜索广播接收线程，控制广播接收的挂起与运行，拒绝响应其它手机的连接
        public int boardCastTime;//广播线程挂起的时间10秒

        private byte[] heartbateData;//心跳包数据（0，0）；

        public FormStartControl(NotifyIcon notifyIcon)
        {
            this.notifyIcon = notifyIcon;
            heartbateData = new byte[] { 0, 0 };
            boardCastTime = 10;

            boardCastThread = new Thread(broadCast);
            boardCastThread.Start();

            //接收指令控制线程
            new Thread(mouseInstruction).Start();
            //计时器线程
            new Thread(timeDown).Start();


        }

        //用于接收搜索广播的线程方法
        private void broadCast()
        {
            //初始化一个Scoket协议，用来接收手机端搜索广播
            Socket socket = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);
            //初始化一个侦听局域网内部所有IP和指定端口
            IPEndPoint iep = new IPEndPoint(IPAddress.Any, 48080);

            mobileEp = (EndPoint)iep;
            socket.Bind(iep);//绑定这个实例

            byte[] buffer = new byte[2];//设置缓冲数据流
            while (true)
            {
                //接收数据,并确把数据设置到缓冲流buffer里面，还可以保存手机ip到ep变量
                socket.ReceiveFrom(buffer, ref mobileEp);
                //获取mouseCtrlSocket的端口，作为发送数据
                byte[] localPortData = System.BitConverter.GetBytes(localIp.Port);
                //用mouseCtrlSocket将数据发送到ep的手机端，手机端解析mouseCtrlSocket获取指令socket的ip和监听端口
                mouseCtrlSocket.SendTo(localPortData, 2, SocketFlags.None, mobileEp);
            }
        }


        //接收鼠标指令的线程方法
        private void mouseInstruction()
        {   
            mouseCtrlSocket = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);
            //指定为0，自动分配本地端口
            IPEndPoint localEP = new IPEndPoint(IPAddress.Any, 0);
            mouseCtrlSocket.Bind(localEP);

            //本地的监听的地址和端口号
            localIp = (IPEndPoint)mouseCtrlSocket.LocalEndPoint;
            //悬浮在托盘图标显示的文字
            notifyIcon.Text = "触摸板后台程序" + "\n本地端口号：" + localIp.Port;

            byte[] buffer = new byte[2];
            while (true)
            {
                mouseCtrlSocket.Receive(buffer);

                //将接收到的buffer数据从byte[]转成int[]
                int[] ints = Utils.bytesToInts(buffer);
                //将int[]指令组解析成指令类型
                Instruction instruction = Utils.dataToInstruction(ints);

                //判断指令
                if (instruction == Instruction.HEARTBEAT)//心跳包
                {
                    //回复心跳包响应，将心跳包（0，0）反馈数据发送到远程端点手机端
                    mouseCtrlSocket.SendTo(heartbateData, 2, SocketFlags.None, mobileEp);
                }
                else
                {
                    //控制鼠标移动
                    MouseEventCtrl.mouseEventCtrl(instruction, ints[0], ints[1]);
                }
                //重置计时器，每次接收到信息，就重置是否初始化接收其他手机搜索广播的需求
                boardCastTime = 10;
                if (boardCastThread.ThreadState != ThreadState.Suspended)
                    //广播线程挂起线程
                    boardCastThread.Suspend();
            }
        }

        /// <summary>
        /// 经过boardCastTime秒后，接收广播的线程继续运行
        /// </summary>
        private void timeDown()
        {
            while (true)
            {
                if (boardCastTime > 0)
                    boardCastTime--;
                else
                    if (boardCastThread.ThreadState == ThreadState.Suspended)
                        // 开启线程，如果10秒之后还没有控制命令接收，就恢复可扫描状态。
                        boardCastThread.Resume();
                //每一秒判断一次
                Thread.Sleep(1000);
            }
        }
    }
}
