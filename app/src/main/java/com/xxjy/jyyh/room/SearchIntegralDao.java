package com.xxjy.jyyh.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xxjy.jyyh.entity.IntegralHistoryEntity;
import com.xxjy.jyyh.entity.SearchHistoryEntity;

import java.util.List;

/**
 * @author power
 * @date 1/31/21 11:12 AM
 * @project ElephantOil
 * @description:
 */
@Dao
public interface SearchIntegralDao {
    //查询所有数据
    @Query("Select * from search_integral")
    List<IntegralHistoryEntity> getAll();

    //删除全部数据
    @Query("DELETE FROM search_integral")
    void deleteAll();

    //一次插入单条数据 或 多条
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IntegralHistoryEntity... historyEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<IntegralHistoryEntity> historyEntities);

    //一次删除单条数据 或 多条
    @Delete
    void delete(IntegralHistoryEntity... historyEntities);

    //一次更新单条数据 或 多条
    @Update
    void update(IntegralHistoryEntity... historyEntities);

    //根据字段去查找数据
    @Query("SELECT * FROM search_integral WHERE integralName= :integralName")
    IntegralHistoryEntity getHistoryByName(String integralName);

    //一次查找多个数据
    @Query("SELECT * FROM search_integral WHERE id IN (:userIds)")
    List<IntegralHistoryEntity> loadAllByIds(List<Long> userIds);

    //多个条件查找
    @Query("SELECT * FROM search_integral WHERE integralName = :name AND id = :id")
    IntegralHistoryEntity getHistoryByName(String name, int id);
}
