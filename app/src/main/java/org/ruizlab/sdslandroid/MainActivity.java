package org.ruizlab.sdslandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import org.ruizlab.sdslandroid.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    TextView tv1;
    TextView tv0 ;
    Button btnStart;

    EditText etInputFileSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


         tv1 = binding.tv1;
         tv0 = binding.tv0;
         etInputFileSize = binding.etFileSize;
         btnStart = binding.btnStart;

        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String fileSizeStr = etInputFileSize.getText().toString();
                int fileSize;
                if(fileSizeStr.isEmpty()){
                    Toast.makeText(getApplicationContext(), " Taking default file size of 1GB", Toast.LENGTH_SHORT).show();
                    fileSize = 1024;
                }
                else
                    fileSize = Integer.valueOf(fileSizeStr);
                tv1.setText("Random File Generation Started");
                btnStart.setEnabled(false);
                startProcess(fileSize);
            }
        });




    }

    private void startProcess(int fileSize) {
        WorkRequest waveletWorkRequest, analyticsWorkRequest = null;
        WorkManager workManager = WorkManager.getInstance(getApplicationContext());

        Data myParameters = new Data.Builder()
                .putStringArray(WaveletProcess.KEY_ARGS, new String[]{String.valueOf(fileSize)})
                .build();

        waveletWorkRequest = new OneTimeWorkRequest.Builder(WaveletProcess.class).setInputData(myParameters).build();
        workManager.enqueue(waveletWorkRequest);
        System.out.println("STARTING");

        analyticsWorkRequest = new OneTimeWorkRequest.Builder(ForegroundAnalytics.class).build();
        workManager.enqueue(analyticsWorkRequest);
        System.out.println("ANALYTICS STARTING");

        workManager.getWorkInfoByIdLiveData(waveletWorkRequest.getId())
                .observe((LifecycleOwner) this, workInfo -> {
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        //Toast.makeText(getApplicationContext(),"CSV Created!", Toast.LENGTH_LONG).show();
                        tv1.setText("Files Generated");
                    }
                });
        workManager.getWorkInfoByIdLiveData(analyticsWorkRequest.getId())
                .observe((LifecycleOwner) this, workInfo -> {
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        Toast.makeText(getApplicationContext(), "ANALYTICS DONE!", Toast.LENGTH_SHORT).show();
                        btnStart.setEnabled(true);
                    }
                });

    }
}