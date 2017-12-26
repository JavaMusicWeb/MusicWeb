package team.musicweb.service;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class PermissionService {
	private static final String PERMISSIONTABLE = "permissions";
	private static MongoCollection<Document> collection;
	private static PermissionService permissionService;

	public static PermissionService getInstance(MongoDatabase mongoDatabase) {
		if (permissionService == null)
			permissionService = new PermissionService(mongoDatabase);
		return permissionService;
	}

	public static PermissionService getInstance() {
		return permissionService;
	}

	private PermissionService(MongoDatabase mongoDatabase) {
		collection = mongoDatabase.getCollection(PERMISSIONTABLE);
	}

	/***
	 * 获取某人的 所有可以访问的 ID，防止使用 cookies 越级访问
	 * 
	 * @param userNumber
	 * @return
	 */
	public String getToken(String userNumber) {
		String token="";
		return token 	;
	}

	/***
	 * 更新某人的所有相关数据 ID
	 * 
	 * @param userNumber
	 */
	public void updateToken(String userNumber) {

	}

}
