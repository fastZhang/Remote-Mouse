using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Microsoft.Win32;

//命名空间
namespace 触摸板系统电脑端
{
    
    //1 该类处理窗口事件，切换自启动，退出功能，
    //2 构造方法打开鼠标控制类
    //3 自启动功能，需要在清单文件添加管理员权限配置    
    public partial class Form1 : Form
    {
        //开机自启动注册表，键的名称 const关键字，相当于java的static final
        private const string boardAutoRunKey = "boardAutoRun";
        private const string autoRunned = "已经开启自启动";
        private const string notAutoRun = "没有开启自启动";

        public Form1()
        {
            InitializeComponent();

            //开启打开鼠标控制类
            new FormStartControl(notifyIcon1);
        }

        //托盘右键打开时，回掉方法，用于判断是否开启自启动，并显示当前状态
        private void contextMenuStrip1_Opening(object sender, CancelEventArgs e)
        {
            //打开自启动注册表
            RegistryKey hkml = Registry.LocalMachine;
            RegistryKey software = hkml.OpenSubKey(@"Software\Microsoft\Windows\CurrentVersion\Run");
            //获取自启动注册表下的所有键值对
            string[] subkeyNames = software.GetValueNames();
            //遍历判断是否有本程序的启动项
            foreach (string keyName in subkeyNames)
            {
                if (keyName == boardAutoRunKey)
                {
                    已经开启自启动ToolStripMenuItem.Text = autoRunned;
                    software.Close();
                    hkml.Close();
                    return;
                }
            }
            已经开启自启动ToolStripMenuItem.Text = notAutoRun;
            software.Close();
            hkml.Close();
            return;
        }

        //已经开启自启动，托盘条目点击方法，切换自启动
        private void 已经开启自启动ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            //获取本app启动路径
            string path = Application.ExecutablePath;
            //创建自启动注册表
            RegistryKey hkml = Registry.LocalMachine;
            RegistryKey software = hkml.CreateSubKey(@"Software\Microsoft\Windows\CurrentVersion\Run");
            if (已经开启自启动ToolStripMenuItem.Text == notAutoRun)
            {
                //设置注册表
                software.SetValue(boardAutoRunKey, path);
            }
            else
            {
                //删除键值对
                software.DeleteValue(boardAutoRunKey, false);
            }
            software.Close();
            hkml.Close();
        }

        //退出程序，托盘条目点击方法
        private void 退出程序ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Environment.Exit(0);
        }
    }
}
