package org.ruizlab.sdslandroid;

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
    public static final String TEST_TYPE = "TEST_TYPE";

    static {
        System.loadLibrary("sdslruiz");
    }

    public WaveletProcess(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    private void createNotificationChannel(String id) {
        NotificationManager notificationManager =
                getApplicationContext().getSystemService(NotificationManager.class);
        if (notificationManager == null) return;

        NotificationChannel channel = new NotificationChannel(
                id,
                "SDSL Processing",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("Running SDSL Test");
        notificationManager.createNotificationChannel(channel);
    }

    private ForegroundInfo createForegroundInfo() {
        String id = "1";
        createNotificationChannel(id);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), id)
                .setContentTitle("Running Test")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .build();

        return new ForegroundInfo(1, notification);
    }

    @Override
    public Result doWork() {
        setForegroundAsync(createForegroundInfo());

        try {
            String testType = getInputData().getString(TEST_TYPE);
            String[] args = getInputData().getStringArray(KEY_ARGS);

            if (testType == null || args == null || args.length == 0) {
                throw new IllegalArgumentException("Invalid input data: Missing test type or arguments.");
            }

            int fileSize = Integer.parseInt(args[0]);

            // Record start time
            ((Global) getApplicationContext()).setCpuTime(SystemClock.currentThreadTimeMillis());
            ((Global) getApplicationContext()).mapperStarts();

            System.out.println("SDSL - Test STARTED: " + testType);
            beginProcess(testType, fileSize);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        } finally {
            // Record end time
            ((Global) getApplicationContext()).setCpuTime(SystemClock.currentThreadTimeMillis());
            ((Global) getApplicationContext()).mapperStops();
        }

        System.out.println("Test FINISHED");
        return Result.success();
    }

    private void beginProcess(String testType, int fileSize) {
        File externalPath = getApplicationContext().getExternalFilesDir(null);
        String filePath = (externalPath != null) ? externalPath.getPath() : "";

        System.out.println("Test Type: " + testType);
        System.out.println("File Path: " + filePath);

        boolean success = false;

        switch (testType) {
            case "RANDOM_FILE_GENERATOR":
                success = randomFileGenerator(filePath, fileSize);
                break;

            case "WAVELET_ENGINE":
                success = waveletEngine(filePath);
                break;

            case "BIT_VECTOR_GENERATOR":
                success = bitVectorGenerator(filePath, fileSize);
                break;

            default:
                System.err.println("Unknown test type: " + testType);
        }

        if (!success) {
            System.err.println(testType + " process failed.");
        } else {
            System.out.println(testType + " process completed successfully.");
        }
    }

    public native boolean randomFileGenerator(String fileStoragePath, int fileSize);
    public native boolean waveletEngine(String fileStoragePath);
    public native boolean bitVectorGenerator(String fileStoragePath, int fileSize);
}
