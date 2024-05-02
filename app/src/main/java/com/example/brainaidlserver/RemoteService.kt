package com.example.brainaidlserver

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class RemoteService : Service() {

    private val mPersonList = mutableListOf<Person?>()

    private val mBinder: Binder = object: IPersonManager.Stub() {

        override fun getPersionList(): MutableList<Person?> = mPersonList

        override fun addPerson(person: Person?): Boolean {
            Log.i("Brain", "RemoteService addPerson in = $person")
            val result = mPersonList.add(person)
            Log.i("Brain", "RemoteService addPerson in result = $mPersonList")
            return result
        }

        override fun addPersonOut(person: Person?): Boolean {
            Log.i("Brain", "RemoteService addPerson out = $person")
            val result = mPersonList.add(person)
            Log.i("Brain", "RemoteService addPerson out result = $mPersonList")
            return result
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("Brain", "RemoteService onCreate......")
        mPersonList.add(Person("Jacky"))
        mPersonList.add(Person("Tommy"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("Brain", "RemoteService onStartCommand.......")
        return super.onStartCommand(intent, flags, startId)
    }
}