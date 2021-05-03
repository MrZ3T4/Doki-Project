package dev.mrz3t4.literatureclub.Jsoup;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import dev.mrz3t4.literatureclub.RecyclerView.Broadcast;
import dev.mrz3t4.literatureclub.RecyclerView.BroadcastAdapter;
import dev.mrz3t4.literatureclub.Utils.Constants;
import dev.mrz3t4.literatureclub.Utils.GenericContext;
import dev.mrz3t4.literatureclub.Utils.NotificationsBuilder;
import dev.mrz3t4.literatureclub.ViewPager.BroadcastFragment;

import static dev.mrz3t4.literatureclub.Utils.Constants.BROADCAST_URL;

public class GetBroadcast {

    private Context context;
    private NotificationsBuilder builder = new NotificationsBuilder();

    public GetBroadcast(Context context) {
        this.context = context;
    }

    public void connect (String url, int mode){

        // Broadcast --> 0 | Season --> 1 | Directory --> 2

        new Thread(()->{

            try {
                Document document = Jsoup.connect(url).userAgent("Mozilla").maxBodySize(0).get();

                switch (mode){
                    case 0:
                        broadcast(document);
                        break;
                    case 1:
                        season(document);
                        break;
                    case 2:

                        break;

                }

            } catch (IOException e) {
                e.printStackTrace();
                builder.createToast("Error al intentar establecer conexión...", Toast.LENGTH_SHORT);
            }

        }).start();

    }

    public void broadcast (Document document){

            try {
                Elements doc = document.select("article[class=col-6 col-sm-4 col-md-3]");

                ArrayList<Broadcast> broadcastArrayList = new ArrayList<>();

                for (Element body:doc){

                    String title = body.select("h2").text();
                    String episode = Constants.episode + " " + body.select("span[class=episode]").text();
                    String img =(body.select("img[class=img-fluid]").attr("src"));
                    String url = body.select("a").attr("href");
                    String type = body.select("span[class=vista2]").text();

                    Log.v("Broadcast: ", title);

                    broadcastArrayList.add(new Broadcast(title, episode,img,url,type));
                }

                Intent intent = new Intent("Simulcast");
                intent.putParcelableArrayListExtra("arraylist", broadcastArrayList);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            } catch (Exception e){
                Log.e("BROADCAST", "<---- Check Exception---->");
                e.printStackTrace();
            }


    }

    public void season (Document document){


    }

    public void directory (Document document){


    }




}
