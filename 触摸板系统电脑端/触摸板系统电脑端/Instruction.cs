using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace 触摸板系统电脑端
{   
    //枚举指令协议类型
    public enum Instruction
    {
        HEARTBEAT,//心跳包

        MOVE, //移动鼠标
        LEFTDOWN, //鼠标左键按下
        LEFTUP, //鼠标左键抬起
        MIDDLEDOWN, //鼠标中键按下
        MIDDLEUP, //鼠标中键抬起
        RIGHTDOWN, //鼠标右键按下
        RIGHTUP //鼠标右键抬起       
    }
}
