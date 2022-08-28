package com.hy0417sage.notes.test.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MemoDao {
    @Query("SELECT * FROM Memo")
    List<Memo> getAll();

    @Query("SELECT * FROM Memo WHERE id IN (:memoIds)")
    List<Memo> loadAllByIds(int[] memoIds);

    @Insert
    void insertAll(Memo memo);

    @Delete
    void delete(Memo memo);
}