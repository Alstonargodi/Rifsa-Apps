package com.example.rifsa_mobile.model.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.rifsa_mobile.model.entity.remote.disease.DiseasePostResponse
import com.example.rifsa_mobile.model.entity.remote.disease.restapivm.NewDiseaseResultRespon
import com.example.rifsa_mobile.model.entity.remote.finance.FinancePostBody
import com.example.rifsa_mobile.model.entity.remote.finance.FinancePostResponse
import com.example.rifsa_mobile.model.entity.remote.finance.FinanceResultResponse
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestPostBody
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestPostResponse
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResultRespon
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryPostResponse
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryResultRespon
import com.example.rifsa_mobile.model.entity.remote.login.LoginBody
import com.example.rifsa_mobile.model.entity.remote.login.LoginResponse
import com.example.rifsa_mobile.model.entity.remote.signup.RegisterBody
import com.example.rifsa_mobile.model.entity.remote.signup.RegisterResponse
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialFirebaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestFirebaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryFirebaseEntity
import com.example.rifsa_mobile.model.remote.ApiService
import com.example.rifsa_mobile.model.remote.FirebaseService
import com.example.rifsa_mobile.utils.FetchResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.UploadTask
import okhttp3.MultipartBody

class RemoteRepository(
    private val firebaseService: FirebaseService,
    private val apiService: ApiService,
) {

    fun authLogin(email : String,password : String): Task<AuthResult> =
        firebaseService.authLogin(email, password)

    fun insertUpdateHarvestResult(data : HarvestFirebaseEntity, userId : String): Task<Void> =
        firebaseService.insertUpdateHarvestResult(data,userId)

    fun queryHarvestResult(userId: String): DatabaseReference {
        return firebaseService.queryHarvestResult(userId)
    }

    fun deleteHarvestResult(date : String,dataId : String, userId : String): Task<Void>{
        return firebaseService.deleteHarvestResult(date, dataId, userId)
    }

    fun insertUpdateFinancial(data : FinancialFirebaseEntity, userId: String): Task<Void>{
        return firebaseService.insertUpdateFinancial(data, userId)
    }

    fun queryFinancial(userId: String): DatabaseReference{
        return firebaseService.queryFinancial(userId)
    }

    fun deleteFinancial(date : String,dataId : String, userId : String): Task<Void>{
        return firebaseService.deleteFinancial(date, dataId, userId)
    }

    fun uploadInventoryFile(name : String, fileUri : Uri, userId: String): UploadTask {
        return firebaseService.uploadInventoryFile(name, fileUri, userId)
    }

    fun insertInventory(data : InventoryFirebaseEntity,userId: String): Task<Void>{
        return firebaseService.insertInventory(data, userId)
    }

    fun readInventory(userId: String): DatabaseReference{
        return firebaseService.readInventory(userId)
    }

    fun deleteInventoryFile(name : String, userId: String): Task<Void>{
        return firebaseService.deleteInventoryFile(name, userId)
    }

    fun deleteInventory(date : String,dataId : String,userId: String): Task<Void> {
        return firebaseService.deleteInventory(date, dataId, userId)
    }

    fun readFarming(userId: String): DatabaseReference{
        return firebaseService.readFarmingData(userId)
    }





































    suspend fun postLogin(data : LoginBody): LiveData<FetchResult<LoginResponse>> =
        liveData {
            emit(FetchResult.Loading)
                try {
                    apiService.postLogin(data).apply {
                        emit(FetchResult.Success(this))
                    }
                }catch (e : Exception){
                    emit(FetchResult.Error(e.message.toString()))
                }
    }

    suspend fun postRegister(data : RegisterBody): LiveData<FetchResult<RegisterResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.postRegister(data).apply {
                    emit(FetchResult.Success(this))
                }
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
    }


    suspend fun getDiseaseRemote(token: String): LiveData<FetchResult<NewDiseaseResultRespon>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(
                   apiService.getDiseaseRemote(token)
                ))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun getDiseaseRemoteById(token: String,id : Int): LiveData<FetchResult<NewDiseaseResultRespon>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(
                    apiService.getDiseaseRemoteById(id, token)
                ))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun postDiseasePredictionRemote(
        file: MultipartBody.Part,
        latitude : Double,
        longitude: Double,
    ): LiveData<FetchResult<DiseasePostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(
                    apiService.predictionDisease(
                        file,
                        latitude,
                        longitude
                    )
                ))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }


    suspend fun deleteDiseaseRemote(id : Int,token: String): LiveData<FetchResult<DiseasePostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(
                    apiService.deleteDiseaseRemote(id,token)
                ))
            }catch (e : Exception){
                emit(FetchResult.Error(
                    e.message.toString()
                ))
            }
        }
}