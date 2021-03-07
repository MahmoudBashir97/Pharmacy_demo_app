package com.mahmoudbashir.pharmacy_app.Api_Interface;

import com.mahmoudbashir.pharmacy_app.models.RequestData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface api_Interface {

    //لما تبعت الداتا مع بعض زي مثلا عبارة عن package
    @Headers({"Authorization: key=AAAAdLTFJ9g:APA91bGFwvhFIFFpUg-8QXaEFVH8tr7ltaf-8vqL7R1bmuuxghG8hbERIaS9iDo-N4rsWT-2KZ8oUUdIh8MPgxAckP6bvyZ2yKeNjl2SGfopiV7OzwvcX7BDVTIlvnFc5CMfo3KXd39C",
            "Content-Type:application/json"})
    @POST("fcm/send")
    public Call<RequestData> storedata(@Body RequestData data);

/*
    //لما تبعت الداتا مع بعض زي مثلا عبارة عن package
    @Headers({"Authorization: key=AAAAWgnVk88:APA91bFtPXAiE9tx8V_SmqGvxj36-sLpniex0SpoacQvejdBDVRSLFvK_NOH2bKV-H9pB6H3QZkzCbylCX-B-CWgxTj5dWRst6uhB8Fi7GZI1xXFAtfs_RyMfOY-1zHmHDnRlQ0vBAzP",
            "Content-Type:application/json"})
    @POST("fcm/send")
    public Call<com.chatho.chatho.Model.send> chatRequest(@Body com.chatho.chatho.Model.send send);*/
}
