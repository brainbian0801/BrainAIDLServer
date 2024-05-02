package com.example.brainaidlserver

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

class Person(var name: String? = "") : Parcelable {

    constructor(parcel: Parcel?) : this(parcel?.readString())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        Log.i("Brain", "Person.kt writeToParcel..........")
        dest?.writeString(name)
    }

    fun readFromParcel(parcel: Parcel) {
        Log.i("Brain", "Person.kt readFromParcel..........")
        this.name = parcel.readString()
    }

    override fun toString(): String {
        return "Person($name) hashCode = ${hashCode()}"
    }

    companion object CREATOR : Parcelable.Creator<Person> {

        override fun createFromParcel(source: Parcel?): Person {
            return Person(source)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }
}