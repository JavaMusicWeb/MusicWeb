package team.musicweb.moudle;

import java.util.UUID;

public class Music implements BaseModle{
	private String _id;
	private String name;
	private String songer;
	private String md5Value;
	
	public Music() {
		_id=UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	@Override
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSonger() {
		return songer;
	}
	public void setSonger(String songer) {
		this.songer = songer;
	}
	public String getMd5Value() {
		return md5Value;
	}
	public void setMd5Value(String md5Value) {
		this.md5Value = md5Value;
	}
	
	
	@Override
	public String toString() {
		return "Music [_id=" + _id + ", name=" + name + ", songer=" + songer + ", md5Value=" + md5Value + "]";
	}
	

}
