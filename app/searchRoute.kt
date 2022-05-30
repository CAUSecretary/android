@FormUrlEncoded
    @POST("/search/weigh")
    fun searchRoute(
        @Field("endPoint") endPoint:String,
        @Field("startLat") curLat:String,
        @Field("startLon") curLon:String
    ) : Call<String>