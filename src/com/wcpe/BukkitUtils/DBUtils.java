package com.wcpe.BukkitUtils;
import java.sql.*;
import java.util.*;
/**
 * 数据库工具类
 */
public class DBUtils {
    private Connection con = null;

    public DBUtils(String path) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("load driver failure");
        }
        try {
            con = DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建表
     * @param name 表名
     * @param list 格式:列名 数据类型
     * @return void
     * @Author: WCPE
     * @Date: 2020/4/25 9:16
     */
    public void createTable(String name, List<String> list) {
        try {
            Statement stat = con.createStatement();
            StringBuilder sql = new StringBuilder("create table " + name);
            sql.append("(");
            for (String s : list) {
                sql.append(s);
                sql.append(",");
            }
            sql.delete(sql.length() - 1, sql.length());
            sql.append(")");
            stat.execute(sql.toString());
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 添加表中一列
     * @param name 表名
     * @param s 格式:列名 数据类型
     * @return void
     * @Author: WCPE
     * @Date: 2020/4/25 9:20
     */
    public void addTableColumn(String name, String s) {
        try {
            Statement stat = con.createStatement();
            StringBuilder sql = new StringBuilder("alter table " + name + " add "+s);
            stat.execute(sql.toString());
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置表的字符集
     * @param name 表名
     * @param character 字符集名称
     * @return void
     * @Author: WCPE
     * @Date: 2020/4/25 9:20
     */
    public void setTableCharacter(String name, String character) {
        try {
            Statement stat = con.createStatement();
            StringBuilder sql = new StringBuilder("alter table " + name + " character set " + character);
            stat.execute(sql.toString());
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重命名表
     * @param name 表名称
     * @param newName 表新名称
     * @return
     * @Author: WCPE
     * @Date: 2020/4/25 9:20
     */
    public void renameTable(String name, String newName) {
        try {
            Statement stat = con.createStatement();
            StringBuilder sql = new StringBuilder("alter table " + name + " rename to " + newName);
            stat.execute(sql.toString());
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 重命名列名以及数据类型
     * @param table 表名
     * @param column 列名
     * @param newColumn 新列名
     * @param data 新数据类型
     * @return
     * @Author: WCPE
     * @Date: 2020/4/25 9:20
     */
    public void editTableColumn(String table, String column, String newColumn, String data) {
        try {
            Statement stat = con.createStatement();
            StringBuilder sql = new StringBuilder("alter table " + table + " change " + column + " " + newColumn + " " + data);
            stat.execute(sql.toString());
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重命名一列数据类型
     * @param table 表名
     * @param column 列名
     * @param data 新数据类型
     * @return void
     * @Author: WCPE
     * @Date: 2020/4/25 9:20
     */
    public void editTableColumn(String table, String column, String data) {
        try {
            Statement stat = con.createStatement();
            StringBuilder sql = new StringBuilder("alter table " + table + " modify " + column + " " + data);
            stat.execute(sql.toString());
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除表
     * @param name 表名
     * @return void
     * @Author: WCPE
     * @Date: 2020/4/25 9:20
     */
    public void dropTable(String name) {
        try {
            Statement stat = con.createStatement();
            StringBuilder sql = new StringBuilder("drop table if exists " + name);
            stat.execute(sql.toString());
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入一条记录
     * @param table 表名
     * @param list 对应的值列表
     * @return boolean
     * @Author: WCPE
     * @Date: 2020/4/25 9:20
     */
    public boolean insertData(String table, List<Object> list) {
        StringBuilder sql = new StringBuilder("insert into " + table + " values ");
        sql.append("(");
        for (int i = 0; i < list.size(); i++) {
            sql.append("?");
            sql.append(",");
        }
        sql.delete(sql.length() - 1, sql.length());
        sql.append(")");
        try {
            PreparedStatement pst = con.prepareStatement(sql.toString());
            int a = 1;
            for (Object s :list) {
                pst.setObject(a++, s);
            }
            return pst.executeUpdate() == 1 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新一条记录
     * @param table 表名
     * @param valueList 格式: 列名 = 值
     * @param condition 条件 列名
     * @param conditionValue 条件 值
     * @return void
     * @Author: WCPE
     * @Date: 2020/4/25 9:20
     */
    public void upData(String table, List<String> valueList,String condition,String conditionValue) {
        try {
            Statement stat = con.createStatement();
            StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");
            for (String s : valueList) {
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() - 2, sql.length());
            sql.append(" WHERE "+condition+" = "+conditionValue);
            stat.execute(sql.toString());
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param
     * @return
     * @Author: WCPE
     * @Date: 2020/4/25 9:20
     */
    public ResultSet queryAll(String table) {
        String sql_insert = "select * from " + table;
        try {
            PreparedStatement pst = con.prepareStatement(sql_insert);
            return pst.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
