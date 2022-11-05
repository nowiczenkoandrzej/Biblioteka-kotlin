# Library App 

Library app allows you create account and scroll throught book posted by other users. You can also post your own book.

All books posted by users are stored on my own backend server which was created in Django and deployed on Heroku.

[Django Repository](https://github.com/nowiczenkoandrzej/Bibioteka)

App was built with libraries such as:
* Retrofit2
* Hilt
* Picasso

with MVVM architecture




```kotlin
 @Multipart
    @POST("postbook/")
    suspend fun postBook(
        @Part("title") title: RequestBody,
        @Part("author") author: RequestBody,
        @Part("cover") cover: RequestBody,
        @Part("publisher") publisher: RequestBody,
        @Part("dateOfPublishing") dateOfPublishing: RequestBody,
        @Part("dateOfRelease") dateOfRelease: RequestBody,
        @Part("amountOfPages") amountOfPages: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("user") user: RequestBody
    )
```
