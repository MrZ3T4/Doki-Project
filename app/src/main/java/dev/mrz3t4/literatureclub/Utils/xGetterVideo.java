package dev.mrz3t4.literatureclub.Utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;

import java.util.ArrayList;

import dev.mrz3t4.literatureclub.Reproductor;

public class xGetterVideo {

    private String URL;

    public xGetterVideo(String link) {
        this.URL = link;
    }

    public void getXGetterUrl(){

        LowCostVideo xGetter = new LowCostVideo(GenericContext.getContext());
        xGetter.onFinish(new LowCostVideo.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<XModel> vidURL, boolean multiple_quality) {
                URL = vidURL.get(0).getUrl();
                System.out.println(URL);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(URL), "video/mp4");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GenericContext.getContext().startActivity(intent);

            }

            @Override
            public void onError() {
                NotificationsBuilder builder = new NotificationsBuilder();
                builder.createToast("Video no disponible. Intenta con otro enlace.", Toast.LENGTH_SHORT);
            }
        });

        xGetter.find(URL);
    }


}
