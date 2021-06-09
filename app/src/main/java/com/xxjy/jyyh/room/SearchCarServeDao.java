package com.xxjy.jyyh.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xxjy.jyyh.entity.SearchCarServeHistoryEntity;
import com.xxjy.jyyh.entity.SearchHistoryEntity;

import java.util.List;

/**
 * @author power
 * @date 12/11/20 2:48 PM
 * @project RunElephant
 * @description:
 */

@Dao
public interface SearchCarServeDao {
    //查询所有数据
    @Query("Select * from search_car_serve")
    List<SearchCarServeHistoryEntity> getAll();

    //删除全部数据
    @Query("DELETE FROM search_car_serve")
    void deleteAll();

    //一次插入单条数据 或 多条
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchCarServeHistoryEntity... historyEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SearchCarServeHistoryEntity> historyEntities);

    //一次删除单条数据 或 多条
    @Delete
    void delete(SearchCarServeHistoryEntity... historyEntities);

    //一次更新单条数据 或 多条
    @Update
    void update(SearchCarServeHistoryEntity... historyEntities);

    //根据字段去查找数据
    @Query("SELECT * FROM search_car_serve WHERE storeName= :storeName")
    SearchCarServeHistoryEntity getHistoryByName(String storeName);

    //一次查找多个数据
    @Query("SELECT * FROM search_car_serve WHERE id IN (:userIds)")
    List<SearchCarServeHistoryEntity> loadAllByIds(List<Long> userIds);

    //多个条件查找
    @Query("SELECT * FROM search_car_serve WHERE storeName = :name AND id = :id")
    SearchCarServeHistoryEntity getHistoryByName(String name, int id);
}
