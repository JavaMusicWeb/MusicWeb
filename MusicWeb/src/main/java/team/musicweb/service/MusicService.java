package team.musicweb.service;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public class MusicService {
	private MongoCollection<Document> collection;
	
	public MusicService() {}
	public MusicService(MongoCollection<Document> collection) {
		this.collection=collection;
	}
	
	
	/**
	 * 判断服务器是否已经有这首歌曲
	 * @param md5Value
	 * @return
	 */
	public boolean hasMusic(String md5Value) {
		return false;
	}
	
	
	
	/**
	 * 删除歌单歌曲
	 * @param request
	 * @return
	 */
	public boolean remove(HttpServletRequest request) {
		
		return false;
	}
	
	
	
	/**
	 * 通过MD5迁移歌曲到歌单
	 * @param request
	 * @return
	 */
	public boolean transmusic(HttpServletRequest request) {
		return false;
	}
	
	
	/**
	 * 上传歌曲并且保存到歌单
	 * @param request
	 * @return
	 */
	public boolean upload(HttpServletRequest request) {
		//request 中 url qurystirng should do url decode.
		
		return false;
	}

}
