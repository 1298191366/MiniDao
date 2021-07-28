package org.jeecgframework.minidao.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jeecgframework.minidao.pagehelper.PageException;
import org.jeecgframework.minidao.pagehelper.dialect.AbstractHelperDialect;
import org.jeecgframework.minidao.pagehelper.dialect.PageAutoDialect;
import org.jeecgframework.minidao.pojo.MiniDaoPage;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

/**
 * 
 * @Title:JdkLocalUtil
 * @description:JdkLocalUtil
 * @author 张代浩
 * @date Jul 5, 2013 2:58:29 PM
 * @version V1.0
 */
public class MiniDaoUtil {
	private static final Log logger = LogFactory.getLog(MiniDaoUtil.class);
	/**
	 * 缓存有没有问题
	 */
	static PageAutoDialect pageAutoDialect = new PageAutoDialect();

	/**
	 * 数据库类型别名
	 */
	public static final String DATABSE_TYPE_HSQLDB = "hsqldb";
	public static final String DATABSE_TYPE_H2 = "h2";
	public static final String DATABSE_TYPE_PHOENIX = "phoenix";
	public static final String DATABSE_TYPE_POSTGRE = "postgresql";

	public static final String DATABSE_TYPE_MYSQL = "mysql";
	public static final String DATABSE_TYPE_MARIADB = "mariadb";
	public static final String DATABSE_TYPE_SQLITE = "sqlite";
	public static final String DATABSE_TYPE_HERDDB = "herddb";

	public static final String DATABSE_TYPE_ORACLE = "oracle";
	public static final String DATABSE_TYPE_ORACLE9I= "oracle9i";
	public static final String DATABSE_TYPE_DM = "dm"; //达梦数据库
	public static final String DATABSE_TYPE_DB2 = "db2";
	public static final String DATABSE_TYPE_INFORMIX = "informix";
	public static final String DATABSE_TYPE_INFORMIX_SQLI = "informix-sqli";

	public static final String DATABSE_TYPE_SQLSERVER = "sqlserver";
	public static final String DATABSE_TYPE_SQLSERVER2012 = "sqlserver2012";

	public static final String DATABSE_TYPE_DERBY= "derby"; //Derby
	public static final String DATABSE_TYPE_EDB= "edb";
	public static final String DATABSE_TYPE_OSCAR = "oscar"; //神通
	public static final String DATABSE_TYPE_KINGBASE = "kingbase";//人大金仓
	public static final String DATABSE_TYPE_CLICKHOUSE = "clickhouse";
	public static final String DATABSE_TYPE_HIGHGO = "highgo";//瀚高数据库
	public static final String DATABSE_TYPE_XUGU = "xugu";//瀚高数据库
	public static final String DATABSE_TYPE_ZENITH = "zenith"; //华为高斯 GaussDB

	public static final String DATABSE_TYPE_POLARDB = "polardb"; //PolarDB

//	/**
//	 * 分页SQL
//	 */
//	public static final String MYSQL_SQL = "select * from ( {0}) sel_tab00 limit {1},{2}"; // mysql
//	public static final String POSTGRE_SQL = "select * from ( {0}) sel_tab00 limit {2} offset {1}";// postgresql
//	public static final String ORACLE_SQL = "select * from (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_>{2}"; // oracle
//	public static final String SQLSERVER_SQL = "select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}"; // sqlserver


	/**
	 * 获取url
	 *
	 * @param dataSource
	 * @return
	 */
	private static String getUrl(DataSource dataSource) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			return conn.getMetaData().getURL();
		} catch (SQLException e) {
			throw new PageException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					//ignore
				}
			}
		}
	}


	/**
	 * 自动获取DB类型
	 * @param dbUrl 数据库连接
	 * @return
	 */
	public static String getDbType(String dbUrl){
		String dbType = pageAutoDialect.getDialectKeyByJdbcUrl(dbUrl);
		if(dbType!=null){
			dbType = dbType.toLowerCase();
		}
		return dbType;
	}

//    /**
//     * 自动获取DB类型
//     * @param dbUrl 数据库连接
//     * @return
//     */
//    public static String getDbType(String dbUrl){
//        String dbType = "";
//		dbUrl = dbUrl.toLowerCase();
//        if(dbUrl.contains(MiniDaoUtil.DATABSE_TYPE_MYSQL)) {
//            dbType = MiniDaoUtil.DATABSE_TYPE_MYSQL;
//        }else if(dbUrl.contains(MiniDaoUtil.DATABSE_TYPE_ORACLE)) {
//            dbType = MiniDaoUtil.DATABSE_TYPE_ORACLE;
//        }else if(dbUrl.contains(MiniDaoUtil.DATABSE_TYPE_SQLSERVER)) {
//            dbType = MiniDaoUtil.DATABSE_TYPE_SQLSERVER;
//        }else if(dbUrl.contains(MiniDaoUtil.DATABSE_TYPE_POSTGRE)) {
//            dbType = MiniDaoUtil.DATABSE_TYPE_POSTGRE;
//        }else if(dbUrl.contains(MiniDaoUtil.DATABSE_TYPE_DM)) {
//			dbType = MiniDaoUtil.DATABSE_TYPE_DM;
//		}else if(dbUrl.contains(MiniDaoUtil.DATABSE_TYPE_MARIADB)) {
//			dbType = MiniDaoUtil.DATABSE_TYPE_MARIADB;
//		}else if(dbUrl.contains(MiniDaoUtil.DATABSE_TYPE_DB2)) {
//			dbType = MiniDaoUtil.DATABSE_TYPE_DB2;
//		}else if(dbUrl.contains(MiniDaoUtil.DATABSE_TYPE_SQLITE)) {
//			dbType = MiniDaoUtil.DATABSE_TYPE_SQLITE;
//		}else if(dbUrl.contains(MiniDaoUtil.DATABSE_TYPE_KINGBASE8)) {
//			dbType = MiniDaoUtil.DATABSE_TYPE_KINGBASE8;
//		}else if(dbUrl.contains(MiniDaoUtil.DATABSE_TYPE_POLARDB)) {
//			dbType = MiniDaoUtil.DATABSE_TYPE_POLARDB;
//		}else if(dbUrl.contains(MiniDaoUtil.DATABSE_TYPE_OSCAR)) {
//			dbType = MiniDaoUtil.DATABSE_TYPE_OSCAR;
//		}else if(dbUrl.contains(MiniDaoUtil.DATABSE_TYPE_ZENITH)) {
//			dbType = MiniDaoUtil.DATABSE_TYPE_ZENITH;
//		}else if(dbUrl.contains(MiniDaoUtil.DATABSE_TYPE_DERBY)) {
//			dbType = MiniDaoUtil.DATABSE_TYPE_DERBY;
//		}else {
//            dbType = MiniDaoUtil.DATABSE_TYPE_OTHER;
//        }
//        return dbType;
//    }


	/**
	 * 自动获取DB类型
	 * @param dataSource
	 * @return
	 */
	public static String getDbType(DataSource dataSource){
        long startTime=System.currentTimeMillis();
		//获取数据库连接url
		String dbUrl = getUrl(dataSource);
		String dbType = getDbType(dbUrl);
        long endTime=System.currentTimeMillis();
        logger.info("获取DB类型："+ dbType+ "，耗时："+ (endTime-startTime) +"ms");
		return dbType;
	}

		/**
	 * 按照数据库类型，封装SQL
	 *
	 * @param dbUrl
	 *            数据库类型
	 * @param sql
	 * @param page
	 * @param rows
	 * @return
	 */
	public static String createPageSql(String dbUrl, String sql, int page,int rows) {
		AbstractHelperDialect dialect = pageAutoDialect.getDialect(dbUrl);
		MiniDaoPage pageSetting = new MiniDaoPage();
		pageSetting.setPage(page);
		pageSetting.setRows(rows);
		String executePageSql = dialect.getPageSql(sql,pageSetting);
		return executePageSql;
	}

//	/**
//	 * 按照数据库类型，封装SQL
//	 *
//	 * @param dbType
//	 *            数据库类型
//	 * @param sql
//	 * @param page
//	 * @param rows
//	 * @return
//	 */
//	public static String createPageSql(String dbType, String sql, int page,int rows) {
//		int beginNum = (page - 1) * rows;
//		String[] sqlParam = new String[3];
//		sqlParam[0] = sql;
//		sqlParam[1] = beginNum + "";
//		sqlParam[2] = rows + "";
//		if (dbType == null || "".equals(dbType)) {
//			throw new RuntimeException("org.jeecgframework.minidao.aop.MiniDaoHandler:(数据库类型:dbType)没有设置,请检查配置文件");
//		}
//		if (DATABSE_TYPE_OTHER.equals(dbType)) {
//			return sql;
//		}else if (DATABSE_TYPE_MYSQL.equals(dbType) || DATABSE_TYPE_MARIADB.equals(dbType)) {
//			sql = MessageFormat.format(MYSQL_SQL, sqlParam);
//		} else if (DATABSE_TYPE_POSTGRE.equals(dbType)) {
//			sql = MessageFormat.format(POSTGRE_SQL, sqlParam);
//		} else {
//			int beginIndex = (page - 1) * rows;
//			int endIndex = beginIndex + rows;
//			sqlParam[2] = Integer.toString(beginIndex);
//			sqlParam[1] = Integer.toString(endIndex);
//			if (DATABSE_TYPE_ORACLE.equals(dbType) || DATABSE_TYPE_DM.equals(dbType)) {
//				sql = MessageFormat.format(ORACLE_SQL, sqlParam);
//			} else if (DATABSE_TYPE_SQLSERVER.equals(dbType)) {
//				sqlParam[0] = sql.substring(getAfterSelectInsertPoint(sql));
//				sql = MessageFormat.format(SQLSERVER_SQL, sqlParam);
//			}else{
//				//TODO 不兼容的数据库，SQL不分页
//				return sql;
//			}
//		}
//		return sql;
//	}

	/**
	 * 
	 * @param sql
	 * @return
	 */
	private static int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf("select");
		int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
		return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
	}

	/**
	 * 返回首字母变为小写的字符串
	 * 
	 * @param name
	 * @return
	 */
	public static String getFirstSmall(String name) {
		name = name.trim();
		if (name.length() >= 2) {
			return name.substring(0, 1).toLowerCase() + name.substring(1);
		} else {
			return name.toLowerCase();
		}

	}

	/**
	 * 根据SQL_URL读取SQL文件内容
	 * 
	 * @param sqlurl
	 * @return
	 */
	public static String getMethodSqlLogicJar(String sqlurl) {
		StringBuffer sb = new StringBuffer();
		// 返回读取指定资源的输入流
		InputStream is = MiniDaoUtil.class.getResourceAsStream(sqlurl);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String s = "";
		try {
			while ((s = br.readLine()) != null)
				sb.append(s + " ");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 判断方法是否是抽象方法
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isAbstract(Method method) {
		int mod = method.getModifiers();
		return Modifier.isAbstract(mod);
	}

	/**
	 * 判断Class是否是基本包装类型
	 * 
	 * @param clz
	 * @return
	 */
	public static boolean isWrapClass(Class<?> clz) {
		try {
			return ((Class<?>) clz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}

	public static void main(String[] args) throws Exception {
		logger.debug(isWrapClass(Long.class));
		logger.debug(isWrapClass(Integer.class));
		logger.debug(isWrapClass(String.class));
	}
}
