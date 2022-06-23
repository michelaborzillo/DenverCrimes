package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
		
			e.printStackTrace();
			return null ;
		}
	}
	
	
	public List<String> getVertici(String type, int mese ){
		String sql="SELECT DISTINCT offense_type_id "
				+ "FROM EVENTS "
				+ "WHERE offense_category_id=? AND MONTH(reported_date)=?";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection() ;

		try {
			
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, type);
			st.setInt(2, mese);
			ResultSet res=st.executeQuery();
			while (res.next()) {
				String s= res.getString("offense_type_id");
				result.add(s);
			}
			conn.close();
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
	}
	
	
	public List<Adiacenza> getArchi (String categoria, int mese) {
		/*conto quanti quartieri sono uguali, specifico una delle due tabelle perchè ho detto che sono uguali*/
		String sql ="SELECT e1.offense_type_id AS v1, e2.offense_type_id AS v2, COUNT(DISTINCT e1.neighborhood_id) AS peso "
				+ "FROM EVENTS e1, EVENTS e2 "
				+ "WHERE e1.offense_type_id>e2.offense_type_id AND e1.offense_category_id=? AND e1.offense_category_id=e2.offense_category_id "
				+ "AND MONTH(e1.reported_date)=? AND MONTH(e1.reported_date)=MONTH(e2.reported_date) AND e1.neighborhood_id=e2.neighborhood_id "
				+ "GROUP BY e1.offense_type_id, e2.offense_type_id";
		
		List<Adiacenza> result= new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection() ;

		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setInt(2, mese);
			ResultSet res=st.executeQuery();
			while (res.next()) {
				Adiacenza a= new Adiacenza (res.getString("v1"), res.getString("v2"), res.getInt("peso"));
				result.add(a);
			}
			conn.close();
			return result;
			
			
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}


	public List<String> getCategorie() {
		String sql ="SELECT DISTINCT offense_category_id "
				+ "FROM events";
		
		List<String> result= new ArrayList<String>();
		Connection conn = DBConnect.getConnection() ;

		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res=st.executeQuery();
			while (res.next()) {
				result.add(res.getString("offense_category_id"));
			}
			
			conn.close();
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	
	

}
