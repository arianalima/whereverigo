package com.example.bernardojr.whereverigo.gui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appodeal.ads.UserSettings;
import com.example.bernardojr.whereverigo.R;
import com.example.bernardojr.whereverigo.dominio.Local;
import com.example.bernardojr.whereverigo.dominio.Pessoa;
import com.example.bernardojr.whereverigo.infra.ImagemRetangular;
import com.example.bernardojr.whereverigo.negocio.LocalService;
import com.example.bernardojr.whereverigo.negocio.SessaoUsuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private RecyclerView recyclerView;
    private HomeActivity.LocaisAdapter adapter;
    private List<Local> locais = new ArrayList<>();
    private final String appKey = "30b007486c65d1794e143bbcd4189a5f30d27ab806598290";

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.locais_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(null);
        recyclerView.addItemDecoration(new HomeActivity.SpacesItemDecoration(24));

        UserSettings userSettings = Appodeal.getUserSettings(this);
        Pessoa pessoaLogada = SessaoUsuario.getInstancia().getPessoaLogada();
        userSettings.setBirthday(pessoaLogada.getStrDataNascimento());
        if (pessoaLogada.getSexo() == "Feminino"){
            userSettings.setGender(UserSettings.Gender.FEMALE);
        }else {
            userSettings.setGender(UserSettings.Gender.MALE);
        }

        Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL | Appodeal.BANNER | Appodeal.MREC);
        Appodeal.setTesting(true);
        Appodeal.show(this, Appodeal.BANNER_BOTTOM);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        Appodeal.onResume(this, Appodeal.BANNER);
        requesLocais(getApplicationContext());
    }

    private boolean gettingOut = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (gettingOut) {
                super.onBackPressed();
                finish();
            }else{
                gettingOut = true;
                Appodeal.show(this, Appodeal.INTERSTITIAL);

                Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
                    @Override
                    public void onInterstitialLoaded(boolean b) {

                    }

                    @Override
                    public void onInterstitialFailedToLoad() {

                    }

                    @Override
                    public void onInterstitialShown() {

                    }

                    @Override
                    public void onInterstitialClicked() {

                    }

                    @Override
                    public void onInterstitialClosed() {
                        onBackPressed();
                    }
                });
            }
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_logout) {
            Intent i = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_questionario) {
            startQuestionarioActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void startQuestionarioActivity() {
        Intent i = new Intent(HomeActivity.this,QuestionarioActivity.class);
        startActivity(i);
    }

    private class LocaisAdapter extends RecyclerView.Adapter<HomeActivity.LocaisHolder>{

        private List<Local> locais;



        public LocaisAdapter(List<Local> locais) {
            this.locais = locais;
        }

        @Override
        public HomeActivity.LocaisHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.locais_item_adapter, parent, false);

            HomeActivity.LocaisHolder view = new HomeActivity.LocaisHolder(v);
            return view;
        }

        @Override
        public void onBindViewHolder(HomeActivity.LocaisHolder holder, int position) {
            holder.cidade.setText(locais.get(position).getCidade());
            holder.estadoPais.setText(locais.get(position).getEstadoPais());
            holder.imagem.setImageResource(locais.get(position).getImagem());
            holder.descricao.setText(locais.get(position).getDescricao());
        }

        @Override
        public int getItemCount() {
            return locais.size();
        }
    }

    private class LocaisHolder extends RecyclerView.ViewHolder{

        private TextView cidade;
        private TextView estadoPais;
        private ImageView imagem;
        private TextView descricao;

        public LocaisHolder(View itemView) {
            super(itemView);
            cidade = (TextView) itemView.findViewById(R.id.locais_cidade);
            estadoPais = (TextView) itemView.findViewById(R.id.locais_estado_pais);
            imagem = (ImagemRetangular) itemView.findViewById(R.id.locais_imagem);
            descricao = (TextView) itemView.findViewById(R.id.locais_descricao);
        }
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {

            outRect.right = space;
            outRect.top = space;
            outRect.left = space;

        }
    }

    public void requesLocais(final Context context){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.31.191:8080/WhereverIGo/rest/LocalService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LocalService usuarioService = retrofit.create(LocalService.class);

        int id = SessaoUsuario.getInstancia().getPessoaLogada().getId();
        Call<ArrayList<Local>> locaisCall = usuarioService.getUltimaPesquisa(id);

        locaisCall.enqueue(new Callback<ArrayList<Local>>() {
            @Override
            public void onResponse(Call<ArrayList<Local>> call, Response<ArrayList<Local>> response) {
                if(response.isSuccessful()){

                    Local local;
                    ArrayList<Local> novosLocais = response.body();
                    for(int i = 0; i < novosLocais.size(); i++){
                        local = novosLocais.get(i);
                        int id = context.getResources().getIdentifier(local.getStrImagem(), "drawable", context.getPackageName());
                        local.setImagem(id);
                    }

                    adapter = new HomeActivity.LocaisAdapter(novosLocais);
                    recyclerView.setAdapter(adapter);

                }else {
                    Toast.makeText(context, response.code() ,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Local>> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
            }

        });

    }

}
