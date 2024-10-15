// -----------------------------------------------------------------------
// <copyright file="WaveletProcess.java" company="Ruiz HCI Lab">
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
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;

public class WaveletProcess extends Worker {

    public static final String KEY_ARGS = "ARGS";


    // Used to load the 'sdslruiz' library on application startup.
    static {
        System.loadLibrary("sdslruiz");
    }

    public WaveletProcess(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public void beginProcess(int fileSize) {
        File externalPath = getApplicationContext().getExternalFilesDir(null);
        String filePath="";
        if(externalPath!=null)
            filePath = externalPath.getPath();
        randomFileGenerator(filePath,fileSize);
        waveletEngine(filePath);
    }

    /**
     * A native method that is implemented by the 'sdslruiz' native library,
     * which is packaged with this application.
     */
    public native boolean randomFileGenerator(String fileStoragePath, int fileSize);

    public native boolean waveletEngine(String fileStoragePath);

    private void createChannel(String id) {
        CharSequence name = "SDSL Wavelet Forest";
        String description = "SDSL";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
    private ForegroundInfo createForegroundInfo() {

        Context context = getApplicationContext();
        String id = "1";
        String title = "SDSL Wavelet";

        /*
        String cancel = context.getString(R.string.cancel_download);
        // This PendingIntent can be used to cancel the worker
        PendingIntent intent = WorkManager.getInstance(context)
                .createCancelPendingIntent(getId());
         */

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
        return new ForegroundInfo(Integer.parseInt(id),notification,FOREGROUND_SERVICE_TYPE_DATA_SYNC);//api 34 problems anisha
    }
    @NonNull
    @Override
    public Result doWork() {
        setForegroundAsync(createForegroundInfo());
        try {
            String[] str = getInputData().getStringArray(KEY_ARGS);
            ((Global)this.getApplicationContext()).setCpuTime(SystemClock.currentThreadTimeMillis());
            ((Global)this.getApplicationContext()).mapperStarts();
            System.out.println("SDSL - Wavelet STARTED");
            beginProcess(Integer.valueOf(str[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Indicate whether the work finished successfully with the Result
        ((Global)this.getApplicationContext()).setCpuTime(SystemClock.currentThreadTimeMillis());
        ((Global)this.getApplicationContext()).mapperStops();
        System.out.println("Wavelet FINISHED");
        return Result.success();
    }
}
