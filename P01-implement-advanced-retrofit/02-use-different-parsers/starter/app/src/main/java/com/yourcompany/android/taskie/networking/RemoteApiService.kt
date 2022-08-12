package com.yourcompany.android.taskie.networking

import com.yourcompany.android.taskie.model.Task
import com.yourcompany.android.taskie.model.request.AddTaskRequest
import com.yourcompany.android.taskie.model.request.UserDataRequest
import com.yourcompany.android.taskie.model.response.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Holds the API calls for the Taskie app.
 */
interface RemoteApiService {

  @POST("/api/register")
  fun registerUser(@Body request: UserDataRequest): Call<RegisterResponse>

  @GET("/api/note")
  fun getNotes(@Header("Authorization") token: String): Call<GetTasksResponse>

  @POST("/api/login")
  fun loginUser(@Body request: UserDataRequest): Call<LoginResponse>

  @GET("/api/user/profile")
  fun getMyProfile(@Header("Authorization") token: String): Call<UserProfileResponse>

  @POST("/api/note/complete")
  fun completeTask(
      @Header("Authorization") token: String,
      @Query("id") noteId: String): Call<CompleteNoteResponse>

  @POST("/api/note")
  fun addTask(
      @Header("Authorization") token: String,
      @Body request: AddTaskRequest): Call<Task>
}