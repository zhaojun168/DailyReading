package com.android.util;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;

public class MActivityManager {

	
	private static final MActivityManager INSTANCE = new MActivityManager();

	private static final Stack<Activity> activityStack = new Stack<Activity>();
	
	private MActivityManager() { }
	
	public static MActivityManager getInstance() {
		return INSTANCE;
	}
	
	/**
	 * 获取当前Activity栈中元素个数
	 * @return
	 */
	public int getCount() {
		return activityStack.size();
	}
	
	/**
	 * 添加Activity到栈
	 * @param activity
	 */
	public void addActivityToStack(Activity activity) {
		activityStack.add(activity);
	}
	
	/**
	 * 获取当前Activity（栈顶Activity）
	 * @return
	 */
	public Activity getTop() {
		if(activityStack.isEmpty())
			return null;
		Activity activity = activityStack.lastElement();
		return (Activity) activity;
	}
	
	 /**
     * 获取当前Activity（栈顶Activity） 没有找到则返回null
     */
    public Activity findActivity(Class<?> cls) {
    	Activity activity = null;
        for (Activity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return (Activity) activity;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity() {
    	Activity activity = activityStack.lastElement();
        finishActivity((Activity) activity);
    }
    
    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
        	activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity((Activity) activity);
                break;
            }
        }
    }
    
    /**
     * 结束所有Activity<br />
     * 这个方法非常暴力，在性能好的手机，会把即将启动的 activity 也关掉的。
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
    
    /**
     * 结束其它 Activity <br />
     *
     * @param current 当前的 Activity ，即不需关掉的 Activity
     */
    public void finishOtherActivity(Class<?> current) {
    	for (int i = activityStack.size() - 1; i >= 0; i--) {
    		Activity activity = activityStack.get(i);
            if (null != activity && !activity.getClass().equals(current)) {
            	activity.finish();
            }
        }
    }
    
    /**
     * 结束其它 Activity <br />
     *
     * @param current 当前的 Activity ，即不需关掉的 Activity
     */
    public void finishOtherActivity(Activity activity) {
    	for (int i = activityStack.size() - 1; i >= 0; i--) {
    		Activity curr = activityStack.get(i);
            if (null != curr && curr != activity) {
            	curr.finish();
            }
        }
    }

    /**
     * 应用程序退出
     * 
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            Runtime.getRuntime().exit(-1);
        }
    }
    
}
