package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Arco;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artista;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getRuoli(){
		String sql="SELECT DISTINCT a.role AS ruolo "
				+ "FROM authorship a "
				+ "ORDER BY ruolo";
		List<String> result= new ArrayList<>();
		Connection conn= DBConnect.getConnection();
		try {
			PreparedStatement st= conn.prepareStatement(sql);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				String s= rs.getString("ruolo");
				result.add(s);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("SQL ERROR");
		}
		return result;
	}
	
	public List<Artista> getArtisti(String ruolo,Map<Integer,Artista> idMap){
		String sql="SELECT a.artist_id AS id, a.name AS nome "
				+ "FROM authorship au, artists a "
				+ "WHERE au.artist_id=a.artist_id "
				+ "AND au.role=? "
				+ "GROUP BY id,nome";
		List<Artista> result= new ArrayList<>();
		Connection conn= DBConnect.getConnection();
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				int id= rs.getInt("id");
				String nome=rs.getString("nome");
				Artista a = new Artista(id,nome);
				result.add(a);
				idMap.put(id, a);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("SQL ERROR");
		}
		return result;
	}
	
	
	public List<Arco> getArco(Map<Integer,Artista> idMap,String ruolo){
		String sql="SELECT a1.artist_id AS id1, a2.artist_id AS id2, COUNT(DISTINCT eo1.exhibition_id) AS peso "
				+ "FROM artists a1, artists a2, authorship au1, authorship au2, exhibition_objects eo1, exhibition_objects eo2 "
				+ "WHERE a1.artist_id=au1.artist_id "
				+ "AND a2.artist_id= au2.artist_id "
				+ "AND au1.object_id=eo1.object_id "
				+ "AND au2.object_id=eo2.object_id "
				+ "AND eo1.exhibition_id=eo2.exhibition_id "
				+ "AND au1.role=au2.role "
				+ "AND au1.role=? "
				+ "AND au1.artist_id<au2.artist_id "
				+ "GROUP BY id1,id2";
		List<Arco> result= new ArrayList<>();
		Connection conn= DBConnect.getConnection();
		try {
			PreparedStatement st= conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				int id1= rs.getInt("id1");
				int id2= rs.getInt("id2");
				if(idMap.containsKey(id1) && idMap.containsKey(id2)) {
					Artista a1=idMap.get(id1);
					Artista a2= idMap.get(id2);
					int peso= rs.getInt("peso");
					Arco a = new Arco(a1,a2,peso);
					result.add(a);
				}
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("SQL ERROR");
		}
		return result;
	}
}
