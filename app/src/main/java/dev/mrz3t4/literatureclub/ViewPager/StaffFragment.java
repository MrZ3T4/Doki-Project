package dev.mrz3t4.literatureclub.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.RecyclerView.Broadcast;
import dev.mrz3t4.literatureclub.RecyclerView.BroadcastAdapter;
import dev.mrz3t4.literatureclub.RecyclerView.StaffAdapter;
import dev.mrz3t4.literatureclub.Retrofit.Character;
import dev.mrz3t4.literatureclub.Retrofit.InterfaceStaff;
import dev.mrz3t4.literatureclub.Retrofit.Personas;
import dev.mrz3t4.literatureclub.Retrofit.Seiyuu;
import dev.mrz3t4.literatureclub.Retrofit.VoiceActor;
import dev.mrz3t4.literatureclub.Utils.Constants;
import dev.mrz3t4.literatureclub.Utils.NotificationsBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;
import static dev.mrz3t4.literatureclub.Utils.Constants.BROADCAST_URL;

public class StaffFragment extends Fragment {

    private ArrayList<Broadcast> broadcastArrayList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;


    private String BASE_URL = "https://api.jikan.moe/v3/";
    private String ANIME_URL;

    private View view;
    private int count = 0;
    private boolean bingo = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.viewpager_staff, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_staff);
        progressBar = view.findViewById(R.id.progressbar_staff);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(getStaffReceiver,
                new IntentFilter("data"));

        return view;
    }

    BroadcastReceiver getStaffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String id = intent.getStringExtra("ID");

            if (!bingo) {
                bingo = true;
                getStaff(id);
            }
        }
    };



    private void getStaff(String id){

        System.out.println("BINGOOOO from staff is " + bingo);

        try {
            ANIME_URL = BASE_URL.concat("anime/").concat(id).concat("/characters_staff");

            System.out.println("URL_STAFF: " + ANIME_URL);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            InterfaceStaff interfaceStaff = retrofit.create(InterfaceStaff.class);

            interfaceStaff.getPersonas(ANIME_URL).enqueue(new Callback<Personas>() {
                @Override
                public void onResponse(Call<Personas> call, Response<Personas> response) {


                    System.out.println("RESPONSE: " + response.code());


                    if (response.code() == 200) {

                        progressBar.setVisibility(GONE);

                        List<Character> charactersList = response.body().getCharacters();

                        ArrayList<Seiyuu> seiyuuArrayList = new ArrayList<>();

                        for (Character c : charactersList) {

                            Seiyuu seiyuu = new Seiyuu();

                            List<VoiceActor> voiceActors = c.getVoiceActors();

                            String voice, imagen, url;

                            if (voiceActors.size() == 0) {
                                voice = null;
                                imagen = null;

                                seiyuu.setSeiyuu(voice);
                                seiyuu.setSeiyuuImagen(imagen);
                                seiyuuArrayList.add(seiyuu);


                            } else {

                                voice = voiceActors.get(0).getName();
                                imagen = voiceActors.get(0).getImageUrl();
                                url = voiceActors.get(0).getUrl();

                                seiyuu.setSeiyuu(voice);
                                seiyuu.setSeiyuuImagen(imagen);
                                seiyuu.setSeiyuuUrl(url);
                                seiyuuArrayList.add(seiyuu);

                            }

                            bingo = true;
                            call.cancel();

                        }

                        StaffAdapter staffAdapter = new StaffAdapter((ArrayList<Character>) charactersList, seiyuuArrayList, getActivity());
                        recyclerView.setAdapter(staffAdapter);
                        recyclerView.animate().alpha(1f).setDuration(300).start();

                    } else {
                        call.cancel();
                    }
                }

                @Override
                public void onFailure(Call<Personas> call, Throwable t) {
                    call.cancel();
                    System.out.println("Algo salio mal");

                }
            });
        } catch (Exception e){
            progressBar.setVisibility(GONE);
            NotificationsBuilder Builder = new NotificationsBuilder();
            Builder.createToast("Personajes no encontrados", Toast.LENGTH_SHORT);
        }




    }

}