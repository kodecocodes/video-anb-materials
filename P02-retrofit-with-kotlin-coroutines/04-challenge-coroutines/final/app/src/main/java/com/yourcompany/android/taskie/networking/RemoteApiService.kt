package com.yourcompany.android.taskie.networking

import com.yourcompany.android.taskie.model.DeleteNoteResponse
import com.yourcompany.android.taskie.model.Task
import com.yourcompany.android.taskie.model.request.AddTaskRequest
import com.yourcompany.android.taskie.model.request.UserDataRequest
import com.yourcompany.android.taskie.model.response.CompleteNoteResponse
import com.yourcompany.android.taskie.model.response.GetTasksResponse
import com.yourcompany.android.taskie.model.response.LoginResponse
import com.yourcompany.android.taskie.model.response.RegisterResponse
import com.yourcompany.android.taskie.model.response.UserProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Holds the API calls for the Taskie app.
 */
interface RemoteApiService {

  @POST("/api/register")
  fun registerUser(@Body request: UserDataRequest): Call<RegisterResponse>

  @GET("/api/note")
  suspend fun getNotes(): GetTasksResponse

  @POST("/api/login")
  fun loginUser(@Body request: UserDataRequest): Call<LoginResponse>

  @GET("/api/user/profile")
  suspend fun getMyProfile(): UserProfileResponse

  @POST("/api/note/complete")
  suspend fun completeTask(
      @Query("id") noteId: String): CompleteNoteResponse

  @POST("/api/note")
  suspend fun addTask(
      @Body request: AddTaskRequest): Task

  @DELETE("/api/note")
  suspend fun deleteNote(@Query("id") noteId: String): DeleteNoteResponse
}