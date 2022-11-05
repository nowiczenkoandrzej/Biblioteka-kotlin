# Library App 

Library app allows you create account and scroll throught book posted by other users. You can also post your own book.

All books posted by users are stored on my own backend server which was created in Django and deployed on Heroku.

[Django Repository](https://github.com/nowiczenkoandrzej/Bibioteka)

&nbsp;

App was built with libraries such as:
* **Retrofit2**
* **Hilt**
* **Picasso**

with MVVM architecture.

&nbsp;
## Retrofit2

App allows you post post book cover which you can choose from phone's gallery.
Such an operation requires **multipart post request** via **Retrofit** which you can see below.
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
[Source code](https://github.com/nowiczenkoandrzej/Biblioteka-kotlin/blob/master/app/src/main/java/com/nowiczenko/andrzej/biblioteka/retrofit/MyApi.kt)

&nbsp;

Request is executed in app's repository class
```kotlin
override suspend fun postBook(
        body: UploadRequestBody,
        name: String,
        book: PostBookItem
    ): Boolean {
        return try {
            myApi.postBook(
                title = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    book.title
                ),
                ... // other arguments
                ),
                amountOfPages = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    book.amountOfPages
                ),
                image = MultipartBody.Part
                    .createFormData(
                        "image",
                        name,
                        body
                    ),
                user = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    userId
                )
            )
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
```
[Source code](https://github.com/nowiczenkoandrzej/Biblioteka-kotlin/blob/master/app/src/main/java/com/nowiczenko/andrzej/biblioteka/repository/books/BookRepositoryImplementation.kt)

&nbsp;


## Hilt
App is built using **dependency injection** pattern via **Hilt** library.
App contains **AppModele** object where **Retrofit** reference is defined.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): MyApi {
        return MyApi.invoke()
    }

}
```
[Source code](https://github.com/nowiczenkoandrzej/Biblioteka-kotlin/blob/master/app/src/main/java/com/nowiczenko/andrzej/biblioteka/di/AppModule.kt)


&nbsp;

There is also module for creating app's repositories implementation  which are scoped to ViewModels lifecycles.
```kotlin
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun bindUserRepository(
        repositoryImplementation: UserRepositoryImplementation
    ): UserRepository

    @ViewModelScoped
    @Binds
    abstract fun bindBookRepository(
        repositoryImplementation: BookRepositoryImplementation
    ): BooksRepository

}
```
[Source code](https://github.com/nowiczenkoandrzej/Biblioteka-kotlin/blob/master/app/src/main/java/com/nowiczenko/andrzej/biblioteka/di/RepositoryModule.kt)


&nbsp;


## Design patterns

Beside **dependency injection** There are also others design patterns used while creating app such as:

### Singleton
There is one **singleton** used in app. It is single **Retrofit** instance.

```kotlin 
companion object{
        operator fun invoke(): MyApi{
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://biblioteka-gsoft.herokuapp.com/")
                .build()
                .create(MyApi::class.java)
        }
    }
```
[Source code](https://github.com/nowiczenkoandrzej/Biblioteka-kotlin/blob/master/app/src/main/java/com/nowiczenko/andrzej/biblioteka/retrofit/MyApi.kt)

### Factory
There is also **factory** pattern responsible for creating UI state and deciding what is and what is not displayed on the screen.
```kotlin
 lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.users.collect { event ->
                    when(event){
                        is StateEvent.Success<*> -> {
                            setVisibility(true)
                            users = event.result as List<UserItem>?
                        }
                        is StateEvent.Failure -> setVisibility(true)
                        is StateEvent.Loading -> setVisibility(false)
                        is StateEvent.Empty -> Unit
                    }
                }
            }
        }
```
[Source code](https://github.com/nowiczenkoandrzej/Biblioteka-kotlin/blob/master/app/src/main/java/com/nowiczenko/andrzej/biblioteka/ui/login/LoginActivity.kt)




