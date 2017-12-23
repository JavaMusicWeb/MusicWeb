package team.musicweb.moudle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User implements BaseModle {
	
	private String _id;
	private String nikename;
	private String numberid;
	private String password;
	private List<String> musicsheets;
	
	public User() {
		_id=UUID.randomUUID().toString().replaceAll("-", "");
		musicsheets=new ArrayList<String>();
	}
	
	@Override
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getNikename() {
		return nikename;
	}
	public void setNikename(String nikename) {
		this.nikename = nikename;
	}
	public String getNumberid() {
		return numberid;
	}
	public void setNumberid(String numberid) {
		this.numberid = numberid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getMusicsheets() {
		return musicsheets;
	}
	public void setMusicsheets(List<String> musicsheets) {
		this.musicsheets = musicsheets;
	}
	
	@Override
	public String toString() {
		return "User [_id=" + _id + ", nikename=" + nikename + ", numberid=" + numberid + ", password=" + password
				+ ", musicsheets=" + musicsheets + "]";
	}

}
