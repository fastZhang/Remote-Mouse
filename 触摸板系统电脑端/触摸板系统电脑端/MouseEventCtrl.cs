using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace 触摸板系统电脑端
{
    class MouseEventCtrl
    {
        /// <summary>
        /// 控制鼠标触发事件
        /// </summary>
        /// <param name="instruct">鼠标指令</param>
        public static void mouseEventCtrl(Instruction instruct, int x, int y)
        {
            switch (instruct)
            {
                case Instruction.LEFTDOWN:
                    mouse_event(MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0);
                    break;
                case Instruction.LEFTUP:
                    mouse_event(MOUSEEVENTF_LEFTUP, 0, 0, 0, 0);
                    break;
                case Instruction.MIDDLEDOWN:
                    mouse_event(MOUSEEVENTF_MIDDLEDOWN, 0, 0, 0, 0);
                    break;
                case Instruction.MIDDLEUP:
                    mouse_event(MOUSEEVENTF_MIDDLEUP, 0, 0, 0, 0);
                    break;
                case Instruction.RIGHTDOWN:
                    mouse_event(MOUSEEVENTF_RIGHTDOWN, 0, 0, 0, 0);
                    break;
                case Instruction.RIGHTUP:
                    mouse_event(MOUSEEVENTF_RIGHTUP, 0, 0, 0, 0);
                    break;
                case Instruction.MOVE:
                    mouse_event(MOUSEEVENTF_MOVE, x, y, 0, 0);
                    break;
            }
        }

        const int MOUSEEVENTF_MOVE = 0x0001; //移动鼠标

        const int MOUSEEVENTF_LEFTDOWN = 0x0002; //模拟鼠标左键按下
        const int MOUSEEVENTF_LEFTUP = 0x0004; //模拟鼠标左键抬起

        const int MOUSEEVENTF_MIDDLEDOWN = 0x0020;//模拟鼠标中键按下
        const int MOUSEEVENTF_MIDDLEUP = 0x0040; //模拟鼠标中键抬起

        const int MOUSEEVENTF_RIGHTDOWN = 0x0008; //模拟鼠标右键按下
        const int MOUSEEVENTF_RIGHTUP = 0x0010; //模拟鼠标右键抬起
  
        const int MOUSEEVENTF_ABSOLUTE = 0x8000; //标示是否采用绝对坐标

        /// <summary>
        /// 调用Win32 API中的鼠标事件函数，//默认采用相对坐标
        /// </summary>
        /// <param name="dwFlags">标志之一或组合</param>
        /// <param name="dx">指定x方向的绝对位置或相对位置</param>
        /// <param name="dy">指定y方向的绝对位置或相对位置</param>
        /// <param name="dwData">中间滚轮</param>
        /// <param name="dwExtraInfo">指定与鼠标事件相关的附加32位值，用函数GetMessageExtraInfo来获得</param>
        /// <returns></returns>
        [System.Runtime.InteropServices.DllImport("user32")]
        private static extern int mouse_event(int dwFlags, int dx, int dy, int dwData, int dwExtraInfo);

    }
}
