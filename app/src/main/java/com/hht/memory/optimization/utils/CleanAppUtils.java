package com.hht.memory.optimization.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.widget.Toast;

import java.util.List;

/**
 * @author Realmo
 * @version 1.0.0
 * @name HHTService
 * @email momo.weiye@gmail.com
 * @time 2018/11/21 14:12
 * @describe
 */
public class CleanAppUtils {

    public static final String TAG = "CleanAppUtils";

    //白名单
    private static class AppWhiteList{
        //--------------------system 白名单-----------------------
        private static final String APP_SYSTEM = "com.android";
        private static final String APP_HHT = "com.hht";
        private static final String APP_MSTAR = "com.mstar";

        //--------------------custom 白名单-----------------------
    }


    public static void killAll(Context context){

        //获取一个ActivityManager 对象
        ActivityManager activityManager = (ActivityManager)context.
                getSystemService(Context.ACTIVITY_SERVICE);
        //获取系统中所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager
                .getRunningAppProcesses();
        if(appProcessInfos == null || appProcessInfos.size()<1){
            return ;
        }
        //被杀进程计数
        int count=0;
        //记录被杀死进程的包名
        String nameList="";
        //清理前的可用内存
        long beforeMem = getAvailMemory(context);
        //Log.i(TAG, "清理前可用内存为 : " + beforeMem+"M");

        for (ActivityManager.RunningAppProcessInfo appProcessInfo:appProcessInfos) {
            nameList="";
            //跳过白名单应用及当前进程
            if( appProcessInfo.processName.startsWith(AppWhiteList.APP_SYSTEM)||
                    appProcessInfo.processName.startsWith(AppWhiteList.APP_HHT)||
                    appProcessInfo.processName.startsWith(AppWhiteList.APP_MSTAR)||
                    appProcessInfo.pid==android.os.Process.myPid()){
                String[] pkNameList=appProcessInfo.pkgList;
                for(int i=0;i<pkNameList.length;i++){
                    String pkName=pkNameList[i];

                    //Log.d(TAG,"已过滤进程:"+pkName);

                }

                continue;
            }
            //进程下的所有包名
            String[] pkNameList=appProcessInfo.pkgList;
            for(int i=0;i<pkNameList.length;i++){
                String pkName=pkNameList[i];
                //杀死该进程
                activityManager.killBackgroundProcesses(pkName);
                count++;
                nameList+="  "+pkName;
            }
            //Log.i(TAG, nameList+"---------------------");
        }

        long afterMem = getAvailMemory(context);//清理后的内存占用
        Toast.makeText(context, "Release " + (afterMem - beforeMem) + "M memory", Toast.LENGTH_SHORT).show();


    }


    /*
     * 获取可用内存大小 MB
     */
    public static long getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem/ (1024 * 1024);
    }


    /*
     * 获取总内存大小  MB
     */
    public static long getTotalMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.totalMem/ (1024 * 1024);
    }

    /**
     * 可用内存的占比
     * @param context
     * @return
     */
    public static float getAvailMemoryPercent(Context context){
        return getAvailMemory(context)*1f / getTotalMemory(context);
    }


    public static float getUsedMemoryPercent(Context context){
        return 1-getAvailMemoryPercent(context);
    }


}
