package team.musicweb.service;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import team.musicweb.moudle.User;

public class UserService {
	private MongoCollection<Document> collection;
	
	public UserService() {}
	
	public UserService(MongoCollection<Document> collection) {
		this.collection=collection;
	}
	
	/**
	 * 用户注册
	 * @param request
	 * @return 状态码 成功:200   用户名冲突：2001   
	 */
	public int register(HttpServletRequest request) {
		return 0;
	}
	
	
	
	/**
	 * 用户登录
	 * @param request
	 * @return  成功返回对象，否则返回NULL
	 */
	public User login(HttpServletRequest request) {
		return null;
	}
	
	
	/**
	 * 用户信息修改
	 * @param request
	 * @return 成功返回修改后的对象，否则返回NULL
	 */
	public User updateInfo(HttpServletRequest request) {
		return null;
	}
	
	
	
	

}
