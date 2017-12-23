package team.musicweb.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import team.musicweb.moudle.Music;

public class MusicsheetService {
	MongoCollection<Document> collection;
	public MusicsheetService() {}
	
	public MusicsheetService(MongoCollection<Document> collection) {
		this.collection=collection;
	}
	
	
	
	/**
	 * 创建歌单
	 * @param request
	 * @return
	 */
	public boolean create(HttpServletRequest request) {
		//querystring should do url decode!
		return false;
	}
	
	
	/**
	 * 删除歌单
	 * @param HttpServletRequest 
	 * @return
	 */
	public boolean delete(HttpServletRequest request) {
		return false;
	}
	
	
	/**
	 * 获取 歌单集合大小
	 * @return
	 */
	public int getMusicsheetCount() {
		return 0;
	}
	
	/***
	 * 获取某一页歌单
	 * @param currentPage
	 * @param perSize
	 * @return 前端规定格式
	 */
	public List<MusicSheetPack> getAllMusicsheets(HttpServletRequest request,String perSize){
		return null;
	}
	
	
	/***
	 * 获取某人的所有歌单
	 * @param userid
	 * @return
	 */
	public List<MusicSheetPack> getMusicsheetsByUserid(HttpServletRequest request){
		return null;
	}
	
	
	/***
	 * 获取歌单的歌曲
	 * @param musicsheetid
	 * @return
	 */
	public List<Music> getMusics(HttpServletRequest request){
		return null;
	}
	
	
	/**
	 * 评论歌单
	 * @param request
	 * @return
	 */
	public boolean publishComment(HttpServletRequest request) {
		
		return false;
	}
	
	
	/***
	 * 获取歌单评论
	 * @param musicsheetid
	 * @return
	 */
	public List<MusicSheetComment> getComments(HttpServletRequest request){
		return null;
	}
	
	
	/**
	 * 删除评论
	 * @param request
	 * @return
	 */
	public boolean removeComment(HttpServletRequest request) {
		return false;
	}
	
	class MusicSheetComment{
		private String id;
		private String commentorId;
		private String commentorNickName;
		private String time;
		private String content;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getCommentorId() {
			return commentorId;
		}
		public void setCommentorId(String commentorId) {
			this.commentorId = commentorId;
		}
		public String getCommentorNickName() {
			return commentorNickName;
		}
		public void setCommentorNickName(String commentorNickName) {
			this.commentorNickName = commentorNickName;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
	}
	
	class MusicSheetPack{
		private String id;
		private String name;
		private String pictureUrl;
		private String createTime;
		private String totalSongs;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPictureUrl() {
			return pictureUrl;
		}
		public void setPictureUrl(String pictureUrl) {
			this.pictureUrl = pictureUrl;
		}
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		public String getTotalSongs() {
			return totalSongs;
		}
		public void setTotalSongs(String totalSongs) {
			this.totalSongs = totalSongs;
		}
	}
}
