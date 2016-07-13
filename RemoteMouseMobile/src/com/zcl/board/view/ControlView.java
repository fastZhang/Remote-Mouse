package com.zcl.board.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zcl.board.listener.MouseActionListener;
import com.zcl.board.mouse.MouseAction;

/**
 * @author  ZL 
 * @version 2016-5-25 上午10:27:53
 */
public class ControlView extends View{

    /**
     * view的宽
     */
    private int viewWidth;
    
    /**
     * view的高
     */
    private int viewHeight;
    
    /**
     * 按键开始区域的y坐标
     */
    private int btnY;
    
    /**
     * 左键的x坐标结束
     */
    private int btnLeftXend;
    
    /**
     * 中键的x坐标结束
     */
    private int btnMidXend;

    /**
     * 单点 ，点击的坐标x
     */
    private float x;
    
    /**
     * 单点， 点击的坐标y
     */
    private float y;
    
    /**
     * 2点触控下第二个点坐标x
     */
    private float xT = 0;
    
    /**
     * 2点触控下第二个点坐标标y
     */
    private float yT = 0;

    /**
     * 左键down
     */
    private boolean isLeftBtnDown;
    
    /**
     * 中键键down
     */
    private boolean isMidBtnDown;
    
    /**
     * 右键键down
     */
    private boolean isRightBtnDown;
    

    /**
     * 标志，左键按下状态移动触控板，正在拖拽
     */
    private boolean isLeftMove;
    
    /**
     * 标志，当前触摸板是位移模式，点击模式
     */
    private boolean isWY;
    
    
    private MouseActionListener mouseListener;
    /**
     * 设置动作监听
     * @param mouse
     */
    public void setOnMouseActionListener(MouseActionListener mouse) {
        mouseListener = mouse;
    }

    public ControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        viewWidth = getWidth();
        viewHeight = getHeight();

        // 按键区域
        btnY = (int) (viewHeight * 9 / 10f);
        // 左键结束x
        btnLeftXend = (int) (viewWidth * 2 / 5f);
        // 中间结束x
        btnMidXend = (int) (viewWidth * 3 / 5f);

        // 创建画笔
        Paint p = new Paint();

        p.setStyle(Paint.Style.FILL);// 设置填满
        
        if(isLeftBtnDown){
            p.setColor(Color.YELLOW);// 点击黄色
            // 画左键
            canvas.drawRect(0, btnY, btnLeftXend, viewHeight, p);
        } else {
            p.setColor(Color.GRAY);// 设置灰色
            // 画左键
            canvas.drawRect(0, btnY, btnLeftXend, viewHeight, p);
        }
        
        if(isMidBtnDown){
            p.setColor(Color.YELLOW);// 点击黄色
            canvas.drawRect(btnLeftXend, btnY, btnMidXend, viewHeight, p);

        } else {
            // 画中键
            p.setColor(Color.DKGRAY);
            canvas.drawRect(btnLeftXend, btnY, btnMidXend, viewHeight, p);
        }
        
        if(isRightBtnDown){
            p.setColor(Color.YELLOW);// 点击黄色
            // 画右键
            canvas.drawRect(btnMidXend, btnY, viewWidth, viewHeight, p);
        } else {
            p.setColor(Color.GRAY);// 设置灰色
            // 画右键
            canvas.drawRect(btnMidXend, btnY, viewWidth, viewHeight, p);
        }

        p.setColor(Color.RED);// 设置红色
        // 画字迹
        canvas.drawText("左键", (viewWidth * 0.7f / 5f),
                (viewHeight * 9.5f / 10f), p);
        canvas.drawText("中键", (viewWidth * 2.3f / 5f),
                (viewHeight * 9.5f / 10f), p);
        canvas.drawText("右键", (viewWidth * 3.7f / 5f),
                (viewHeight * 9.5f / 10f), p);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:// 单点按下时，回掉
                motionOneDown(event);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 1) {
                    // 单点触控时，不会回掉此方法
                } else if (event.getPointerCount() == 2) {// 记录组合键功能，先按下左键，或右键
                    motionTwoDown(event);
                }
                break;

            case MotionEvent.ACTION_MOVE:// 只处理移动
                motionMove(event);
                break;

            case MotionEvent.ACTION_UP:
                motionUp(event);
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * 单点按下处理
     */
    private void motionOneDown(MotionEvent event) {
        // 记录单点坐标，单点时候只能是按键事件
        x = event.getX();
        y = event.getY();

        // 左键点击
        if (x < btnLeftXend && y > btnY) {// 点击的坐标在左键区域
            isLeftBtnDown = true;// 左键点击
            mouseListener.sendMouseAction(MouseAction.LEFT_DOWN, 0, 0);
            //区域变色
            invalidate();
            
        }

        // 中键点击
        if (x > btnLeftXend && x < btnMidXend && y > btnY) {
            mouseListener.sendMouseAction(MouseAction.MIDDLE_DOWN, 0, 0);
            //区域变色
            isMidBtnDown = true;
            invalidate();
        }

        // 右键点击
        if (x > btnMidXend && y > btnY) {
            mouseListener.sendMouseAction(MouseAction.RIGHT_DOWN, 0, 0);
            //区域变色
            isRightBtnDown = true;
            invalidate();
        }
    }

    /**
     * 双点按下处理
     */
    @SuppressLint("NewApi") private void motionTwoDown(MotionEvent event) {
        if (!isLeftMove) {// 如果不是左键和移动，组合功能状态
            // 记录第一个点的坐标
            x = event.getX(0);
            y = event.getY(0);
            // 设置左键按下
            if (x < btnLeftXend && y > btnY) {
                isLeftBtnDown = true;
                mouseListener.sendMouseAction(MouseAction.LEFT_DOWN, 0, 0);
            }
            // 设置右键按下
            if (x > btnMidXend && y > btnY) {
                isRightBtnDown = true;
                mouseListener.sendMouseAction(MouseAction.RIGHT_DOWN, 0, 0);
            }
        }
        xT = event.getX(1);
        yT = event.getY(1);
    }

    /**
     * 移动处理
     */
    @SuppressLint("NewApi") private void motionMove(MotionEvent event) {
        if (event.getPointerCount() == 1 && event.getY() < btnY) {// 移动前单点触控板区域
            // 单点触摸板区域，取消左键按下的状态
            if (isLeftBtnDown) {
                mouseListener.sendMouseAction(MouseAction.LEFT_UP, 0, 0);
                isLeftBtnDown = false;
            }

            // 回掉记录位移
            x = (event.getX() - x);
            y = (event.getY() - y);

            if (x == 0 && y == 0) {
                // 单点击模式
                isWY = false;
            } else {
                // 单点位移模式
                isWY = true;
                // 发送位移
                mouseListener.sendMouseAction(MouseAction.MOVE, (int) x, (int) y);
            }
            x = event.getX();
            y = event.getY();

        } else if (event.getPointerCount() == 2) {
            // 多点移动,发送第二点的坐标，且第二点是在触摸板区域移动的组合键功能
            xT = event.getX(1) - xT;
            yT = event.getY(1) - yT;
            if (isLeftBtnDown || isRightBtnDown) {// 左右键组合键功能
                // 拖拽，正在拖拽状态
                isLeftMove = true;
                mouseListener.sendMouseAction(MouseAction.MOVE, (int) xT, (int) yT);
            }
            xT = event.getX(1);
            yT = event.getY(1);
        }
    }

    /**
     * 抬起处理
     */
    private void motionUp(MotionEvent event) {
        // 左键起
        if (x < btnLeftXend && y > btnY) {
            isLeftBtnDown = false;
            isLeftMove = false;
            mouseListener.sendMouseAction(MouseAction.LEFT_UP, 0, 0);
            //区域变色
            invalidate();

        }
        // 中键起
        if (x > btnLeftXend && x < btnMidXend && y > btnY) {
            mouseListener.sendMouseAction(MouseAction.MIDDLE_UP, 0, 0);
            //区域变色
            isMidBtnDown = false;
            invalidate();
        }
        // 右键起
        if (x > btnMidXend && y > btnY) {
            isRightBtnDown = false;
            isLeftMove = false;
            mouseListener.sendMouseAction(MouseAction.RIGHT_UP, 0, 0);
            //区域变色
            isRightBtnDown = false;
            invalidate();
        }

        if (y < btnY && !isWY) {// 如果不是位移模式，而是在触控板区域，快速点击抬起，则是相当于，左键事件
            mouseListener.sendMouseAction(MouseAction.LEFT_DOWN, 0, 0);
            mouseListener.sendMouseAction(MouseAction.LEFT_UP, 0, 0);

        }

        if (y < btnY && isWY) {// 取消位移模式状态
            isWY = false;
        }
        x = 0;
        y = 0;
        xT = 0;
        yT = 0;
    }
}
