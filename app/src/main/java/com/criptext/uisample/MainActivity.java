package com.criptext.uisample;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.criptext.monkeykitui.input.MediaInputView;
import com.criptext.monkeykitui.input.listeners.InputListener;
import com.criptext.monkeykitui.recycler.ChatActivity;
import com.criptext.monkeykitui.recycler.MonkeyAdapter;
import com.criptext.monkeykitui.recycler.MonkeyItem;
import com.criptext.monkeykitui.recycler.audio.DefaultVoiceNotePlayer;
import com.criptext.monkeykitui.recycler.audio.VoiceNotePlayer;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ChatActivity{

    private MonkeyAdapter adapter;
    private RecyclerView recycler;

    private VoiceNotePlayer voiceNotePlayer;

    private MediaInputView mediaInputView;

    SlowMessageLoader loader;
    private SensorHandler sensorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAudioFile();
        createImageFile();

        loader = new SlowMessageLoader(this);
        ArrayList<MonkeyItem> messages = loader.generateRandomMessages();
        adapter = new MonkeyAdapter(this, messages);
        adapter.setHasReachedEnd(false);

        recycler = (RecyclerView) findViewById(R.id.recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
        voiceNotePlayer = new DefaultVoiceNotePlayer(adapter, recycler);
        initInputView();

        sensorHandler = new SensorHandler(voiceNotePlayer, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorHandler.onPause();
    }

    @Override
    protected void onStart(){
        super.onStart();
        voiceNotePlayer.initPlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(voiceNotePlayer!=null && voiceNotePlayer.isPlayingAudio()) {
            voiceNotePlayer.onPauseButtonClicked();
        }
        if(voiceNotePlayer!=null) {
            voiceNotePlayer.releasePlayer();
        }
        sensorHandler.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorHandler.onDestroy();
    }

    public MonkeyAdapter getAdapter() {
        return adapter;
    }

    /**
     * Si no tengo archivos creo uno nuevo.
     */
    private void createAudioFile(){
        File file = new File(getCacheDir() + "/barney.aac");
        if(!file.exists()){
            try {
            InputStream ins = getResources().openRawResource(R.raw.barney);
            FileOutputStream outputStream = new FileOutputStream(file.getPath());

            byte buf[] = new byte[1024];
            int len;

                while ((len = ins.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initInputView(){
        mediaInputView = (MediaInputView) findViewById(R.id.inputView);
        if(mediaInputView!=null) {
            mediaInputView.setInputListener(new InputListener() {
                @Override
                public void onNewItem(@NotNull MonkeyItem item) {
                    MessageItem newItem = new MessageItem("0", item.getMessageId(),
                            item.getMessageText(), item.getMessageTimestamp(), item.isIncomingMessage(),
                            MonkeyItem.MonkeyItemType.values()[item.getMessageType()]);

                    newItem.setStatus(MonkeyItem.OutgoingMessageStatus.read);
                    switch (MonkeyItem.MonkeyItemType.values()[item.getMessageType()]) {
                        case audio: //init audio MessageItem
                            newItem.setDuration(item.getAudioDuration());
                            newItem.setMessageContent(item.getFilePath());
                            break;
                        case photo:
                            Log.d("MainActivity", "new photo");
                            newItem.setMessageContent(item.getFilePath());
                            break;
                    }

                    adapter.smoothlyAddNewItem(newItem, recycler); // Add to recyclerView
                }
            });
            /*
            ONLY IF DEVELOPER DECIDES TO USE HIS OWN OPTIONS FOR LEFT BUTTON
            mediaInputView.getAttachmentHandler().addNewAttachmentButton(new AttachmentButton() {
                @NonNull
                @Override
                public String getTitle() {
                    return "Send Contact";
                }

                @Override
                public void clickButton() {
                    System.out.println("DO SOMETHING!!");
                }
            });
            */
        }
    }

    /**
     * Si no tengo archivos creo uno nuevo.
     */
    private void createImageFile(){
        File file = new File(getCacheDir() + "/mrbean.jpg");
        if(!file.exists()){
            try {
                InputStream ins = getResources().openRawResource(R.raw.mrbean);
                FileOutputStream outputStream = new FileOutputStream(file.getPath());

                byte buf[] = new byte[1024];
                int len;

                while ((len = ins.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        file = new File(getCacheDir() + "/mrbean_blur.jpg");
        if(!file.exists()){
            try {
                InputStream ins = getResources().openRawResource(R.raw.mrbean_blur);
                FileOutputStream outputStream = new FileOutputStream(file.getPath());

                byte buf[] = new byte[1024];
                int len;

                while ((len = ins.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if(mediaInputView!=null && mediaInputView.getCameraHandler()!=null)
            mediaInputView.getCameraHandler().onActivityResult(requestCode,resultCode, data);

    }

    /***OVERRIDE METHODS****/

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public void onFileDownloadRequested(int position, @NotNull MonkeyItem item) {

    }

    @Override
    public void onLoadMoreData(int loadedItems) {
        loader.execute();
    }
}
