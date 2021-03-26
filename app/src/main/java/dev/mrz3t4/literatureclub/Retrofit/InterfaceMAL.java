package dev.mrz3t4.literatureclub.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface InterfaceMAL {

    @GET
    Call<MalID> getID(@Url String anime);

}
