package hcmute.edu.vn.selfalarm.fragments;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import hcmute.edu.vn.selfalarm.MusicService;
import hcmute.edu.vn.selfalarm.R;

public class MusicFragment extends Fragment {
    private MusicService musicService;
    private boolean isBound = false;
    private Uri selectedMusicUri;
    private Button playButton, stopButton;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    selectMusic();
                } else {
                    Toast.makeText(getContext(), "Storage permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> selectMusicLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    selectedMusicUri = result.getData().getData();
                    playButton.setEnabled(true);
                    stopButton.setEnabled(true);
                    Toast.makeText(getContext(), "Music selected", Toast.LENGTH_SHORT).show();
                }
            });

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        Intent intent = new Intent(getActivity(), MusicService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);

        Button selectMusicButton = view.findViewById(R.id.select_music_button);
        playButton = view.findViewById(R.id.play_button);
        stopButton = view.findViewById(R.id.stop_button);

        selectMusicButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                selectMusic();
            }
        });

        playButton.setOnClickListener(v -> {
            if (isBound && selectedMusicUri != null) {
                musicService.playMusic(selectedMusicUri);
            }
        });

        stopButton.setOnClickListener(v -> {
            if (isBound && musicService != null) {
                musicService.stopMusic();
            }
        });

        return view;
    }

    private void selectMusic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        selectMusicLauncher.launch(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBound) {
            getActivity().unbindService(connection);
            isBound = false;
        }
    }
}