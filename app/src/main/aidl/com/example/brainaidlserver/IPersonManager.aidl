// IPersonManager.aidl
package com.example.brainaidlserver;

import com.example.brainaidlserver.Person;
// Declare any non-default types here with import statements

interface IPersonManager {
    List<Person> getPersionList();

    //in: from client to server
    boolean addPerson(in Person person);

    boolean addPersonOut(inout Person person);
}