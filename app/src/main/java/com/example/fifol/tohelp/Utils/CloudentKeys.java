package com.example.fifol.tohelp.Utils;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;

/**
 * Created by shim-polak on 3/12/2018.
 */

public class CloudentKeys {
    public static final String TEXT_API_KEY = "aturedishavingrooletille",
            TEXT_API_SECRET = "b48a197d344b364faef1861d74d4385945f4d49c",
            DB_USER_NAME = "5163dd96-e2e4-42f6-8956-24a8ba1360ab-bluemix";
   public static CloudantClient getClientBuilder (){
       return ClientBuilder.account(DB_USER_NAME).username(TEXT_API_KEY).password(TEXT_API_SECRET).build();
   }
}
