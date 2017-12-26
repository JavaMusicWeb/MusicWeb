package team.musicweb.service;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;

import com.alibaba.fastjson.TypeReference;
import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.eq;
import team.musicweb.moudle.User;
import team.musicweb.util.MongoUtil;

public class UserService {
	private MongoCollection<Document> collection;

	public UserService() {
	}

	public UserService(MongoCollection<Document> collection) {
		this.collection = collection;
	}

	/**
	 * 用户注册
	 * 
	 * @param request
	 * @return 状态码 成功:200 用户名冲突：2001 输入不合法： 20010
	 */
	public int register(HttpServletRequest request) {
		String userNum = request.getParameter("NumberId");
		String userNike = request.getParameter("NikeName");
		String userPwd = request.getParameter("Password");
		// check input value validate
		if (userNum.length() > 20 || userPwd.length() > 20 || userNike.length() > 20)
			return 20010;
		// check userNumer repeat

		User user = MongoUtil.findOne(collection, eq("numberid", userNum), new TypeReference<User>() {
		});
		if (user == null) {
			user = new User();
			user.setNikename(userNike);
			user.setNumberid(userNum);
			user.setPassword(String.valueOf(userPwd.hashCode()));
			if (MongoUtil.insert(collection, user))
				return 200;
			else
				return 500;
		} else
			return 2001;
	}

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @return 成功返回对象，否则返回NULL
	 */
	public User login(HttpServletRequest request) {
		String userNum = request.getParameter("NumberId");
		String userPwd = request.getParameter("Password");
		User user = MongoUtil.findOne(collection, eq("numberid", userNum), new TypeReference<User>() {});
		if (user != null && user.getPassword().equals(String.valueOf(userPwd.hashCode())))
			return user;
		return null;
	}

	/**
	 * 用户信息修改
	 * 
	 * @param request
	 * @return 成功返回修改后的对象，否则返回NULL
	 */
	public User updateInfo(HttpServletRequest request) {
		String userNum = request.getParameter("NubmerId");
		String userNike = request.getParameter("NikeName");
		String userPwd = request.getParameter("Password	");
		User user = MongoUtil.findOne(collection, eq("numberid", userNum), new TypeReference<User>() {});
		if(user!=null) {
			user.setNikename(userNike);
			user.setPassword(String.valueOf(userPwd.hashCode()));
			return user;
		}
		else
			return null;
		
	}

}
