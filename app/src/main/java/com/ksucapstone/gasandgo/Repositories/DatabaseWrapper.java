package com.ksucapstone.gasandgo.Repositories;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseWrapper implements ValueEventListener {

    private static DatabaseReference mDatabaseReference = null;
    private DataSnapshotReceiver mReceiver;

    public DatabaseWrapper(DataSnapshotReceiver receiver) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mReceiver = receiver;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mReceiver.onSnapshotReceived(dataSnapshot);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        //Todo: handle onCancelled errors
    }

    public void queryOnceForSingleObject(String directory) {
        mDatabaseReference.child(directory).addListenerForSingleValueEvent(this);
    }

    public void liveUpdateData(String directory){
        mDatabaseReference.child(directory).addValueEventListener(this);
    }

    public interface DataSnapshotReceiver {
        void onSnapshotReceived(DataSnapshot snapshot);
    }
}
