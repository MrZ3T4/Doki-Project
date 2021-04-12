package dev.mrz3t4.literatureclub;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.mrz3t4.literatureclub.RecyclerView.Anime;
import dev.mrz3t4.literatureclub.RecyclerView.AnimeAdapter;
import dev.mrz3t4.literatureclub.Utils.GenericContext;
import dev.mrz3t4.literatureclub.Utils.JsonTools;

public class SearchActivity extends AppCompatActivity {

    private AnimeAdapter directorioAdapter;
    private ArrayList<Anime> directorioArrayList = new ArrayList<>();

    private EditText mSearchTextView;
    private ImageView mEmptyButton;
    private ImageView mVoiceSearchButton;

    @BindView(R.id.recycler_resultado) RecyclerView recyclerView;
    @BindView(R.id.search_view) MaterialSearchView searchView;
    @BindView(R.id.my_toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        setContentView(R.layout.activity_buscador);

        ButterKnife.bind(this);

        getJSON();
        setUpToolbar();


    }

    private void setUpToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchView.setVoiceSearch(true);
        searchView.setBackgroundResource(R.drawable.tab_divider);
        searchView.showSearch(false);
        searchView.showVoice(true);
        searchView.setHint("Buscar series, ovas, peliculas...");
        searchView.setVoiceIcon(ContextCompat.getDrawable(this, R.drawable.ic_voice));
        searchView.setBackIcon(ContextCompat.getDrawable(this, R.drawable.toolbar_back));
        searchView.setEllipsize(true);

        mEmptyButton =  searchView.findViewById(R.id.action_empty_btn);
        mSearchTextView = searchView.findViewById(R.id.searchTextView);
        ImageView mBackButton = searchView.findViewById(R.id.action_up_btn);
        mVoiceSearchButton = searchView.findViewById(R.id.action_voice_btn);

        mEmptyButton.setImageResource(R.drawable.ic_clear);
        mSearchTextView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorOnSurface, typedValue, true);
        @ColorInt int color = typedValue.data;
        mSearchTextView.setTextColor(color);

        mVoiceSearchButton.setVisibility(View.VISIBLE);

        mSearchTextView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                return true;
            }
            return false;
        });

        mEmptyButton.setOnClickListener(view -> {

            mVoiceSearchButton.setVisibility(View.VISIBLE);
            mEmptyButton.setVisibility(View.GONE);
            searchView.setQuery("",true);

        });

        mBackButton.setOnClickListener(v -> {
            onBackPressed();
           // overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    private void getJSON() {
        JsonTools JSONTools = new JsonTools();

        if (JSONTools.jsonExists()) {
            File directory = new File(GenericContext.getContext().getFilesDir(), "directory.json");

            try {

                FileInputStream fileInputStream = new FileInputStream(directory);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String animesJson = bufferedReader.readLine();

                JSONArray jsonDirectory = new JSONArray(animesJson);

                for (int pos = 0; pos <= jsonDirectory.length(); pos++) {

                    Anime anime = new Anime();

                    JSONObject jsonObject = jsonDirectory.getJSONObject(pos);

                    String title = jsonObject.getString("Title");
                    String img = jsonObject.getString("Cover");
                    String date = jsonObject.getString("Date");
                    String type = jsonObject.getString("Type");
                    String url = jsonObject.getString("Link");


                    anime.setTitle(title);
                    anime.setImg(img);
                    anime.setType(type);
                    anime.setDate(date);
                    anime.setUrl(url);

                    directorioArrayList.add(anime);

                    System.out.println(directorioArrayList.get(pos).getTitle());
                }

            } catch (Exception e){
                e.printStackTrace();
            }

            directorioAdapter = new AnimeAdapter(directorioArrayList, this);
            recyclerView.setItemViewCacheSize(30);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            recyclerView.setHasFixedSize(true);

            recyclerView.setAdapter(directorioAdapter);

    } else {
            Toast.makeText(this,"La busqueda estará disponible despues de obtener el directorio, por favor espere...", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_drawer, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(searchItem);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    //recyclerView.setVisibility(View.GONE);
                } else {
                    //recyclerView.setVisibility(View.VISIBLE);
                }
                directorioAdapter.getFilter().filter(newText);
                return false;
            }
        });
    return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
