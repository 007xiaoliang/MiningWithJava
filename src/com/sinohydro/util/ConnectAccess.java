package com.sinohydro.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTextField;

import com.sinohydro.domain.BlastArea;
import com.sinohydro.domain.CircumcenterCoordinate;
import com.sinohydro.mainWindow.WarningUI;

public class ConnectAccess {
	private Connection con = null;
	private PreparedStatement ps = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	/**
	 * 根据数据库位置连接数据库，读取数据
	 * 
	 * @param path
	 * @throws Exception
	 */
	public void accessOutputUtil(String path, String blastName, JTextField textField) throws Exception {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			con = DriverManager.getConnection("jdbc:ucanaccess://" + path, "", "");
			stmt = con.createStatement();
			rs = stmt.executeQuery("select 爆区代号 from 爆破孔表");
			while (rs.next()) {
				if (rs.getString(1).equals(blastName)) {// 数据库已存在此编号
					new WarningUI("此编号已存在");
					textField.setText(null);// 自动将输入的内容清空
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeAllResource(con, stmt, rs);
		}
	}

	/**
	 * 根据数据库位置连接数据库，存储数据
	 * 
	 * @param path
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public void accessInputUtil(String path, String blastName, Date date, List<BlastArea> list) {
		boolean flag=true;
		try {
			String sql = "insert into 爆破孔表 (爆区代号,工程号,开孔坐标E,开孔坐标N,开孔坐标R,深度,方位角,倾角,日期,取样编号,TCu,Lith,Clay,Remark) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			con = DriverManager.getConnection("jdbc:ucanaccess://" + path, "", "");
			PreparedStatement ps = con.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				try {
					ps.setString(1, blastName);
					ps.setString(2, list.get(i).getHoleNo());
					ps.setObject(3, list.get(i).getCoordinateX());
					ps.setObject(4, list.get(i).getCoordinateY());
					ps.setObject(5, list.get(i).getCoordinateZ());
					ps.setObject(6, list.get(i).getHoleDepth());
					ps.setObject(7, 0);
					ps.setObject(8, -90);
					ps.setObject(9, date);
					ps.setObject(10, list.get(i).getSampleName());
					ps.setObject(11, list.get(i).getCu());
					if (list.get(i).getLith() == null) {
						ps.setObject(12, "PRAP");
					} else {
						ps.setObject(12, list.get(i).getLith());
					}
					String clay = list.get(i).getClay();
					if(clay.equals("1.0")) {
						ps.setObject(13,"LC");
					}else if(clay.equals("2.0")) {
						ps.setObject(13,"M1C");
					}else if(clay.equals("3.0")) {
						ps.setObject(13,"M2C");
					}else if(clay.equals("4.0")) {
						ps.setObject(13,"HC");
					}else {
						flag=false;
						ps.setObject(13,null);
						throw new Exception();
					}
					
					Double remark = list.get(i).getRemark();
					if(remark==1.0) {
						ps.setObject(14,"all ore");
					}else if(remark==0.0) {
						ps.setObject(14,"Waste");
					}else if(remark==0.5){
						ps.setObject(14,"half ore");
					}else {
						double depth=list.get(i).getHoleDepth();
						ps.setObject(14,Math.round(remark*depth)+"米见矿");
					}
					ps.executeUpdate();
				} catch (Exception e) {
					ps = con.prepareStatement("delete * from 爆破孔表 where 爆区代号='" + blastName+"'");
					ps.executeUpdate();
					if(false==flag) {
						new WarningUI("请检查Clay参数是否正确");
					}else {
						new WarningUI("请检查工程号是否重复！");
					}
					e.printStackTrace();
					break;
				}
			}
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from 爆破孔表 where 爆区代号='" + blastName+"'");
			if (rs.next()) {
				new WarningUI("导入成功！");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAllResource(con, ps, rs);
		}
	}

	/**
	 * 读取数据库指定炮区的孔数据，作为圈矿数据源
	 * 
	 * @param path
	 * @throws Exception
	 */
	public List<CircumcenterCoordinate> coordinateSource(String path, String blastName) {
		List<CircumcenterCoordinate> list = new ArrayList<CircumcenterCoordinate>();
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			con = DriverManager.getConnection("jdbc:ucanaccess://" + path, "", "");
			stmt = con.createStatement();
			String sql = "select 开孔坐标E,开孔坐标N,开孔坐标R,TCu from 爆破孔表   where 爆区代号='" + blastName + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				CircumcenterCoordinate cc = new CircumcenterCoordinate();
				cc.setX(rs.getDouble(1));
				cc.setY(rs.getDouble(2));
				cc.setZ(rs.getDouble(3));
				cc.setCu(rs.getDouble(4));
				list.add(cc);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAllResource(con, stmt, rs);
		}
		return list;
	}

	/**
	 * 关闭数据源连接，节省资源消耗
	 * 
	 * @param conn
	 * @param st
	 * @param rs
	 */
	public static void closeAllResource(Connection conn, Statement st, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
