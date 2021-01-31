package com.xxjy.jyyh.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xxjy.jyyh.entity.SearchHistoryEntity;

import java.util.List;

/**
 * @author power
 * @date 12/11/20 2:48 PM
 * @project RunElephant
 * @description:
 */

@Dao
public interface SearchOilDao {
    //查询所有数据
    @Query("Select * from search_oil")
    List<SearchHistoryEntity> getAll();

    //删除全部数据
    @Query("DELETE FROM search_oil")
    void deleteAll();

    //一次插入单条数据 或 多条
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchHistoryEntity... historyEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SearchHistoryEntity> historyEntities);

    //一次删除单条数据 或 多条
    @Delete
    void delete(SearchHistoryEntity... historyEntities);

    //一次更新单条数据 或 多条
    @Update
    void update(SearchHistoryEntity... historyEntities);

    //根据字段去查找数据
    @Query("SELECT * FROM search_oil WHERE gasName= :gasName")
    SearchHistoryEntity getHistoryByName(String gasName);

    //一次查找多个数据
    @Query("SELECT * FROM search_oil WHERE id IN (:userIds)")
    List<SearchHistoryEntity> loadAllByIds(List<Long> userIds);

    //多个条件查找
    @Query("SELECT * FROM search_oil WHERE gasName = :name AND id = :id")
    SearchHistoryEntity getHistoryByName(String name, int id);
}
