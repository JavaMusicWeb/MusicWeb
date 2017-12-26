package team.musicweb.moudle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Comment implements BaseModle {

	private String _id;
	private String userId;
	private String musicshetId;
	private String publishDate;
	private String content;
	
	public Comment() {
		_id=UUID.randomUUID().toString().replaceAll("-", "");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		publishDate=df.format(new Date()).replace('-', '.');
	}
	
	@Override
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMusicshetId() {
		return musicshetId;
	}
	public void setMusicshetId(String musicshetId) {
		this.musicshetId = musicshetId;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Comment [_id=" + _id + ", userId=" + userId + ", musicshetId=" + musicshetId + ", publishDate="
				+ publishDate + ", content=" + content + "]";
	}
	

}
