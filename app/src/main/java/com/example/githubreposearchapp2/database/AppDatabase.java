package com.example.githubreposearchapp2.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RepositoryEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RepositoryDao repositoryDao();
}