package com.sohanram.superstore.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CheckoutEntity.class,AddressEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CheckOutDao checkOutDao();
}
