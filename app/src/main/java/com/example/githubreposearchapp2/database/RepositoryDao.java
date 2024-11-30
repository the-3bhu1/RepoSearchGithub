package com.example.githubreposearchapp2.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRepositories(List<RepositoryEntity> repositories);

    @Query("SELECT * FROM repositories")
    List<RepositoryEntity> getAllRepositories();

    @Query("DELETE FROM repositories")
    void clearRepositories();
}