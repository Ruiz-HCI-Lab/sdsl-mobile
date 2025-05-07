// -----------------------------------------------------------------------
// <copyright file="ForegroundAnalytics.java" company="Ruiz HCI Lab">
// Copyright (c) Ruiz HCI Lab. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the repository root for full license information.
// </copyright>
// -----------------------------------------------------------------------
package org.ruizlab.sdslandroid;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Debug;
import android.os.BatteryManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.ruizlab.sdslandroid.R;

public class ForegroundAnalytics extends Worker{

    public ForegroundAnalytics(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        //super(params);
    }

    @NonNull
    @Override
    public Result doWork() {
        setForegroundAsync(createForegroundInfo());
        ArrayList<String> analyticValues = new ArrayList<>();
        long startTime, endTime, elapsedTime, initialCPUTime, finalCPUTime, totalCPUTime;
        float maxRam = 0;
        float totalRam = 0;
        float currentRam;
        float maxTemp = 0;
        float totalTemp = 0;
        float currentTemp;
        int counter = 0;

        float currentNativeRam;
        float nativeMaxRam = 0;
        float totalNativeRam=0;
        startTime = System.currentTimeMillis();

        /*
        List of analytics:
            [0]- A. Sequence file
            [1]- B. Reference file
            [2]- E. Total wall time
            [3]- F. Total CPU time
            [4]- G. Max RAM usage
            [5]- H. Average RAM usage
            [6]- I. Max temperature
            [7]- J. Average temperature
        */

        android.os.BatteryManager bm = (android.os.BatteryManager) getApplicationContext().getSystemService(Context.BATTERY_SERVICE);
        long initialEnergy = bm.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);

        try {

            System.out.println("ANALYTICS STARTED");
            String fileLocation = getApplicationContext().getExternalFilesDir(null) + "/" + "Analytics.csv";
            System.out.println("SDSL Analysis file location "+fileLocation);
            File analyticsFile = new File(fileLocation);
            boolean fileExists= analyticsFile.exists();
            FileWriter fileWriter;
            if(fileExists)
                fileWriter = new FileWriter(fileLocation,true);
            else
                fileWriter = new FileWriter(fileLocation);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            if(!fileExists)
                writer.write("Time, File Size MB, Total Wall Time, Total CPU Time, Max RAM Usage MB, Average RAM Usage MB, Max RAM Usage (Native) MB, Average RAM Usage (Native) MB, Max Temperature, Average Temperature, Estimated Energy Used (mWh)\r\n");


            initialCPUTime = ((Global) this.getApplicationContext()).getCpuTime();
            System.out.println("Initial cpu time: "+initialCPUTime/1000+"s");
            System.out.println("Initial heap  " + Debug.getNativeHeapAllocatedSize()/(1024*1024)+"MB" );

            while (((Global) this.getApplicationContext()).mapperIsRunning())
            {
                currentRam = (float) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
                currentNativeRam = Debug.getNativeHeapAllocatedSize();
                if (maxRam < currentRam) {
                    maxRam = currentRam;
                }
                if(nativeMaxRam < currentNativeRam){
                    nativeMaxRam = currentNativeRam;
                }
                totalRam += currentRam;
                totalNativeRam+=currentNativeRam;
                currentTemp = getCPUTemperature();
                if (maxTemp < currentTemp) {
                    maxTemp = currentTemp;
                }
                totalTemp += currentTemp;
                counter++;
                Thread.sleep(3000);

                endTime = System.currentTimeMillis();
                elapsedTime = endTime-startTime;

                System.out.println("Analytics read #"+ counter +". Elapsed time: " + elapsedTime/1000 + "s, Current ram: "+ currentRam/(1024*1024)+"MB" +", Max ram: "+ maxRam/(1024*1024)+"MB" + ", Current native ram usage: "+ currentNativeRam/(1024*1024)+"MB" +", native Max ram: "+ nativeMaxRam/(1024*1024)+"MB"  + ", Current temp: "+ currentTemp +", Max temp: "+ maxTemp);
            }

            currentNativeRam = Debug.getNativeHeapAllocatedSize();
            finalCPUTime = ((Global) this.getApplicationContext()).getCpuTime();
            System.out.println("Final cpu time: "+finalCPUTime/1000+"s");
            totalCPUTime = finalCPUTime - initialCPUTime;
            totalRam = totalRam/counter;
            totalTemp = totalTemp/counter;
            totalNativeRam = totalNativeRam/counter;

            endTime = System.currentTimeMillis();
            elapsedTime = endTime-startTime;

            long finalEnergy = bm.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
            long energyUsed = finalEnergy - initialEnergy;
            float energyUsed_mWh = energyUsed / 1000000.0f; // Convert nWh to mWh if not -1
            System.out.println("Estimated Energy Used: " + energyUsed_mWh + " mWh");


            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm");
            Date resultdate = new Date(startTime);

            analyticValues.add(""+sdf.format(resultdate));//0
            File inputFile = new File(getApplicationContext().getExternalFilesDir(null) + "/" + "random_data.bin");
            analyticValues.add(""+inputFile.length()/(1024 * 1024)+" MB");
            analyticValues.add(""+elapsedTime/1000); //2
            analyticValues.add(""+totalCPUTime/1000); //3
            analyticValues.add(""+maxRam/(1024*1024)); //4
            analyticValues.add(""+totalRam/(1024*1024)); //5
            analyticValues.add(""+nativeMaxRam/(1024*1024)); //6
            analyticValues.add(""+totalNativeRam/(1024*1024)); //7
            analyticValues.add(""+maxTemp); //8
            analyticValues.add(""+totalTemp); //9
            analyticValues.add(""+energyUsed_mWh); //10


            for (int i = 0; i < analyticValues.size(); i++) {
                writer.write(analyticValues.get(i));
                if (i != analyticValues.size() - 1) {
                    writer.write(",");
                } else {
                    writer.write("\r\n");
                }
            }
            writer.close();
            System.out.println("Final analytics: Elapsed time: " + elapsedTime/1000 + "s, Cpu time: " + totalCPUTime/1000 + "s, Max ram: "+ maxRam/(1024*1024)+"MB" +", Average ram: "+ totalRam/(1024*1024)+"MB" + ", Current native ram usage: "+ currentNativeRam/(1024*1024)+"MB" +", native Max ram: "+ nativeMaxRam/(1024*1024)+"MB"  + ", Max temp: "+ maxTemp +", Average temp: "+ totalTemp);

            System.out.println("ANALYTICS FINISHED");
            return Result.success();
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

    @NonNull
    private ForegroundInfo createForegroundInfo() {

        Context context = getApplicationContext();
        String id = "2";
        String title = "Analytics";

        createChannel(id);

        Notification notification = new NotificationCompat.Builder(context,id)
                .setContentTitle(title)
                .setTicker(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                //.setSmallIcon(R.drawable.ic_work_notification)
                .setOngoing(true)
                // Add the cancel action to the notification which can
                // be used to cancel the worker
                //.addAction(android.R.drawable.ic_delete, cancel, intent)
                .build();
        return new ForegroundInfo(Integer.parseInt(id),notification,FOREGROUND_SERVICE_TYPE_DATA_SYNC);
    }

    private void createChannel(String id) {
        CharSequence name = "SDSL - Analytics";
        String description = "Analytics Process";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public static float getCPUTemperature()
    {
        Process process;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if(line!=null) {
                float temp = Float.parseFloat(line);
                return temp / 1000.0f;
            }else{
                return 51.0f;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }
}
