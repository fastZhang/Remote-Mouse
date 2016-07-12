using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace 触摸板系统电脑端
{
    class Utils
    {
        public static int[] bytesToInts(byte[] data)
        {
            int x = data[0];
            if (x > 127)
                x = (x | (0xffffff << 8));
            int y = data[1];
            if (y > 127)
                y = (y | (0xffffff << 8));
            //x表示data0的指令，y表示data1的指令
            return new int[] { x, y };
        }

        //将数据协议转换为指令
        public static Instruction dataToInstruction(int[] instruct)
        {
            switch (instruct[1])
            {
                //如果是-128，则是鼠标的按键事件
                case -128:
                    switch (instruct[0])
                    {
                        case 0:
                            return Instruction.HEARTBEAT;
                        case 1:
                            return Instruction.LEFTDOWN;
                        case 2:
                            return Instruction.LEFTUP;
                        case 3:
                            return Instruction.MIDDLEDOWN;
                        case 4:
                            return Instruction.MIDDLEUP;
                        case 5:
                            return Instruction.RIGHTDOWN;
                        case 6:
                            return Instruction.RIGHTUP;
                        //心跳包事件             
                        case -128:
                            return Instruction.HEARTBEAT;

                            //将来进行扩展
                    }
                    break;
            }
            // 不是128，普通移动事件
            return Instruction.MOVE;
        }
    }
}
