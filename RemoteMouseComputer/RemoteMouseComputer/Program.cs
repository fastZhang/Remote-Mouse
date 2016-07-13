using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Threading;

namespace RemoteMouseComputer
{
    static class Program
    {
        /// <summary>
        /// 应用程序的主入口点
        /// 启动应用不显示窗口的托盘程序
        /// 禁止多开
        /// </summary>
        [STAThread]
        static void Main()
        {
            bool isFirst;
            //使用使用互斥量Mutex，使用Mutex类可以创建有名称的互斥，而系统可以识别有名称的互斥
            using (Mutex mutex = new Mutex(true, Application.ProductName, out isFirst))
            {
                //赋予了线程初始所属权，也就是首次使用互斥体
                if (isFirst)
                {
                    Application.EnableVisualStyles();
                    Application.SetCompatibleTextRenderingDefault(false);
                    //创建，但不要显示
                    Form1 form = new Form1();
                    //直接运行不显示
                    Application.Run();
                    //显示
                    //Application.Run(form);
                }
                else
                {
                    MessageBox.Show("应用程序已经打开！");
                }
            }
        }
    }
}
