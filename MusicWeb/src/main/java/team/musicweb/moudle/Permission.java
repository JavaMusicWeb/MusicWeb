package team.musicweb.moudle;

import java.util.UUID;

public class Permission implements BaseModle {

	private String _id;
	private String userNumer;
	private String token;
	
	public Permission() {
		_id=UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	@Override
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getUserNumer() {
		return userNumer;
	}
	public void setUserNumer(String userNumer) {
		this.userNumer = userNumer;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
