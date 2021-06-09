package com.xxjy.jyyh.room;

import androidx.room.Room;

import com.xxjy.jyyh.app.App;

/**
 * @author power
 * @date 2020-01-02 17:11
 * @project CIIP
 * @description:
 */
public class DBInstance {
    private static final String DB_NAME = "room_runElephant";
    public static AppDataBase appDataBase;

    public static AppDataBase getInstance(){
        if(appDataBase==null){
            synchronized (DBInstance.class){
                if(appDataBase==null){
                    appDataBase = Room.databaseBuilder(App.getContext(), AppDataBase.class, DB_NAME)
                            //下面注释表示允许主线程进行数据库操作，但是不推荐这样做。
                            .allowMainThreadQueries()
                            //添加下面这一行,会清空原有数据库数据
//                            .fallbackToDestructiveMigration()
                            //正常更新版本
//                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_1_3)
                            .build();
                }
            }
        }
        return appDataBase;
    }
}
