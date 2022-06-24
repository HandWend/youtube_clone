package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

	private Connection conn;
	private PreparedStatement stmt;
	private ResultSet rs;

	public UserDAO() {
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
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int login(String userID, String userPassword) {
		String sql = "SELECT userPassword FROM user WHERE userID = ? ";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, userID);
			rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getString("userPassword").equals(userPassword)) {
					return 1;// 로그인성공
				} else {
					return 0;// 아이디있고 로그인 실패
				}
			} else {
				return -1; // 아이디 없다.
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return -2;// 오류
	}

	public int join(User user) {
		String sql = " INSERT INTO user VALUES(?, ?, ?, ?, ?) ";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getUserID());
			stmt.setString(2, user.getUserPassword());
			stmt.setString(3, user.getUserName());
			stmt.setString(4, user.getUserGender());
			stmt.setString(5, user.getUserEmail());
			return stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return -1;// 오류
	}

	public class BbsDAO {

		private Connection conn;
		private ResultSet rs;

		// 기본 생성자
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

		// 작성일자 메소드
		public String getDate() {
			String sql = "select now()";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					return rs.getString(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ""; // 데이터베이스 오류
		}

		// 게시글 번호 부여 메소드
		public int getNext() {
			// 현재 게시글을 내림차순으로 조회하여 가장 마지막 글의 번호를 구한다
			String sql = "select bbsID from bbs order by bbsID desc";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					return rs.getInt(1) + 1;
				}
				return 1; // 첫 번째 게시물인 경우
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; // 데이터베이스 오류
		}

		// 글쓰기 메소드
		public int write(String bbsTitle, String userID, String bbsContent) {
			String sql = "insert into bbs values(?, ?, ?, ?, ?, ?)";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, getNext());
				pstmt.setString(2, bbsTitle);
				pstmt.setString(3, userID);
				pstmt.setString(4, getDate());
				pstmt.setString(5, bbsContent);
				pstmt.setInt(6, 1); // 글의 유효번호
				return pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; // 데이터베이스 오류
		}

	}
}
