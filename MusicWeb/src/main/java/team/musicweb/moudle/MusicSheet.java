package team.musicweb.moudle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MusicSheet implements BaseModle {

	private String _id;
	private String name;
	private String picUrl;
	private String createTime;
	private List<String> musics;
	private List<String> comments;
	public MusicSheet() {
		_id=UUID.randomUUID().toString().replaceAll("-", "");
		musics=new ArrayList<String>();
		setComments(new ArrayList<String>());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		createTime=df.format(new Date()).replace('-', '.');
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

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public List<String> getMusics() {
		return musics;
	}

	public void setMusics(List<String> musics) {
		this.musics = musics;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	public String getCreateTime() {
		return createTime;
	}
	
	
	

}
