package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BbsDAO {
	
	private Connection conn;
	private PreparedStatement stmt;
	private ResultSet rs;

	public BbsDAO() {
		try {
			String url = "jdbc:mysql://localhost:3306/youtube_clone?characterEncoding=UTF-8&serverTimezone=Asia/Seoul";
			String user = "root";
			String password = "smart";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void dbClose() {
		try {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(conn != null) conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//게시글 번호 부여 메소드
		public int getNext() {
			//현재 게시글을 내림차순으로 조회하여 가장 마지막 글의 번호를 구한다
			String sql = "select bbsID from bbs order by bbsID desc";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getInt(1) + 1;
				}
				return 1; //첫 번째 게시물인 경우
			}catch (Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류
		}
	
	public int write(Bbs bbs) {
		String sql = "insert into bbs values(null, ?, ?, now(), ?, ?)";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bbs.getBbsTitle());
			stmt.setString(2, bbs.getUserID());
			stmt.setString(3, bbs.getBbsContent());
			stmt.setInt(4, 1);
			
			return stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}		
		return -1;
	}
	
	//게시글 리스트 메소드
		public ArrayList<Bbs> getList(int pageNumber){
			String sql = "select * from bbs where bbsID < ? and bbsAvailable = 1 order by bbsID desc limit 10";
			ArrayList<Bbs> list = new ArrayList<Bbs>();
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					Bbs bbs = new Bbs();
					bbs.setBbsID(rs.getInt("bbsID"));
					bbs.setBbsTitle(rs.getString("bbsTitle"));
					bbs.setUserID(rs.getString("userID"));
					bbs.setBbsDate(rs.getString("bbsDate"));
					list.add(bbs);
				}
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				dbClose();
			}	
			return list;
		}
		
		public boolean nextPage(int pageNumber) {
			String sql = "SELECT * from bbs WHERE bbsID < ? and bbsAvailable = 1";
			
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, getNext() - (pageNumber -1)*10);
				rs = stmt.executeQuery();
				if(rs.next()) {
					return true;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return false;
		}
	
}
