package com.xxjy.jyyh.room;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.xxjy.jyyh.entity.IntegralHistoryEntity;
import com.xxjy.jyyh.entity.SearchHistoryEntity;

import java.util.Iterator;
import java.util.List;

/**
 * @author power
 * @date 2020-01-02 17:08
 * @project CIIP
 * @description:
 */
@Database(entities = {SearchHistoryEntity.class, IntegralHistoryEntity.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract SearchOilDao getSearchHistoryDao();

    public List<SearchHistoryEntity> getSearchHistory() {
        return getSearchHistoryDao().getAll();
    }

    public void insertData(String gasName) {
        if (historyDataForward(gasName)) {
            return;
        }
        getSearchHistoryDao().insert(new SearchHistoryEntity(gasName));
    }

    public void upDateData(String gasName) {
        getSearchHistoryDao().update(new SearchHistoryEntity(gasName));
    }

    public void deleteAllData() {
        getSearchHistoryDao().deleteAll();
    }

    /**
     * 历史数据前移
     *
     * @return 返回true表示查询的数据已存在，只需将其前移到第一项历史记录，否则需要增加新的历史记录
     */
    private boolean historyDataForward(String gasName) {
        //重复搜索时进行历史记录前移
        List<SearchHistoryEntity> searchHistory = getSearchHistory();
        Iterator<SearchHistoryEntity> iterator = searchHistory.iterator();
        //不要在foreach循环中进行元素的remove、add操作，使用Iterator模式
        while (iterator.hasNext()) {
            SearchHistoryEntity historyEntity = iterator.next();
            if (historyEntity.getGasName().equals(gasName)) {
                searchHistory.remove(historyEntity);
                searchHistory.add(0, historyEntity);
                getSearchHistoryDao().deleteAll();
                for (int i = 0; i < searchHistory.size(); i++) {
                    searchHistory.get(i).setId(i + 1);
                }
                getSearchHistoryDao().insertAll(searchHistory);
                return true;
            }
        }
        return false;
    }

    public abstract SearchIntegralDao getSearchIntegralHistoryDao();

    public List<IntegralHistoryEntity> getSearchIntegralHistory() {
        return getSearchIntegralHistoryDao().getAll();
    }

    public void insertIntegralData(String name) {
        if (historyDataForward(name)) {
            return;
        }
        getSearchIntegralHistoryDao().insert(new IntegralHistoryEntity(name));
    }

    public void upDateIntegralData(String name) {
        getSearchIntegralHistoryDao().update(new IntegralHistoryEntity(name));
    }

    public void deleteAllIntegralData() {
        getSearchIntegralHistoryDao().deleteAll();
    }

    /**
     * 历史数据前移
     *
     * @return 返回true表示查询的数据已存在，只需将其前移到第一项历史记录，否则需要增加新的历史记录
     */
    private boolean historyDataForward1(String name) {
        //重复搜索时进行历史记录前移
        List<IntegralHistoryEntity> searchHistory = getSearchIntegralHistory();
        Iterator<IntegralHistoryEntity> iterator = searchHistory.iterator();
        //不要在foreach循环中进行元素的remove、add操作，使用Iterator模式
        while (iterator.hasNext()) {
            IntegralHistoryEntity historyEntity = iterator.next();
            if (historyEntity.getIntegralName().equals(name)) {
                searchHistory.remove(historyEntity);
                searchHistory.add(0, historyEntity);
                getSearchHistoryDao().deleteAll();
                for (int i = 0; i < searchHistory.size(); i++) {
                    searchHistory.get(i).setId(i + 1);
                }
                getSearchIntegralHistoryDao().insertAll(searchHistory);
                return true;
            }
        }
        return false;
    }

    //数据库变动添加Migration，简白的而说就是版本1到版本2改了什么东西
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //告诉user表，增添一个String类型的字段 job
            database.execSQL("ALTER TABLE user ADD COLUMN job TEXT");
            //数据库的具体变动，我是在之前的user表中添加了新的column，名字是age。
            //类型是integer，不为空，默认值是0
            database.execSQL("ALTER TABLE user "
                    + " ADD COLUMN age INTEGER NOT NULL DEFAULT 0");
        }
    };
}
