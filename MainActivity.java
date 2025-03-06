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
    TextView tv0;
    Button btnStart;
    EditText etInputFileSize;

    // Example: Hardcoded test type (could be made dynamic in the UI)
    private final String TEST_TYPE = "BIT_VECTOR_GENERATOR"; // Options: BIT_VECTOR_GENERATOR, RANDOM_FILE_GENERATOR, WAVELET_ENGINE

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
                if (fileSizeStr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Taking default file size of 1GB", Toast.LENGTH_SHORT).show();
                    fileSize = 1024; // Default to 1GB
                } else {
                    fileSize = Integer.parseInt(fileSizeStr);
                }
                tv1.setText("Process Started");
                btnStart.setEnabled(false);
                startProcess(TEST_TYPE, fileSize);
            }
        });
    }

    private void startProcess(String testType, int fileSize) {
        WorkManager workManager = WorkManager.getInstance(getApplicationContext());

        // Prepare Wavelet Process input data
        Data waveletParameters = new Data.Builder()
                .putString(WaveletProcess.TEST_TYPE, testType) // Pass the test type
                .putStringArray(WaveletProcess.KEY_ARGS, new String[]{String.valueOf(fileSize)})
                .build();

        // Queue Wavelet Process
        WorkRequest waveletWorkRequest = new OneTimeWorkRequest.Builder(WaveletProcess.class)
                .setInputData(waveletParameters)
                .build();
        workManager.enqueue(waveletWorkRequest);
        System.out.println("Wavelet Process STARTING");

        // Queue Analytics Process
        WorkRequest analyticsWorkRequest = new OneTimeWorkRequest.Builder(ForegroundAnalytics.class)
                .build();
        workManager.enqueue(analyticsWorkRequest);
        System.out.println("ANALYTICS STARTING");

        // Observe Wavelet Process Completion
        workManager.getWorkInfoByIdLiveData(waveletWorkRequest.getId())
                .observe((LifecycleOwner) this, workInfo -> {
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        tv1.setText("Wavelet Process Completed");
                    } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                        tv1.setText("Wavelet Process Failed");
                    }
                });

        // Observe Analytics Completion
        workManager.getWorkInfoByIdLiveData(analyticsWorkRequest.getId())
                .observe((LifecycleOwner) this, workInfo -> {
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        Toast.makeText(getApplicationContext(), "ANALYTICS DONE!", Toast.LENGTH_SHORT).show();
                        btnStart.setEnabled(true);
                    } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                        Toast.makeText(getApplicationContext(), "ANALYTICS FAILED!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
