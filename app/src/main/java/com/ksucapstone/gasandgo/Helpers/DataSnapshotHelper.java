package com.ksucapstone.gasandgo.Helpers;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class DataSnapshotHelper<T> {

    public T getSnapshotValue(DataSnapshot snapshot, Class<T> classType) {
        return snapshot.getValue(classType);
    }

    public ArrayList<T> getObjectListFromSnapshot(DataSnapshot receivedDataSnapshot, Class<T> classType) {
        ArrayList<T> list = new ArrayList<>();
        for (DataSnapshot dataSnapshot : receivedDataSnapshot.getChildren()) {
            T object = dataSnapshot.getValue(classType);
            list.add(object);
        }
        return list;
    }
}
