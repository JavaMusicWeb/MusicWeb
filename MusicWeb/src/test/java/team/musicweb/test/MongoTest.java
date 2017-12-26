package team.musicweb.test;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jws.soap.SOAPBinding.Use;
import javax.print.Doc;

import org.apache.taglibs.standard.lang.jstl.EqualityOperator;
import org.bson.Document;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

import team.musicweb.moudle.Comment;
import team.musicweb.moudle.Music;
import team.musicweb.moudle.User;
import team.musicweb.util.MongoUtil;

public class MongoTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

			@SuppressWarnings("resource")
			MongoClient mongoClient=new MongoClient();
			MongoDatabase mongoDatabase=mongoClient.getDatabase("musicweb");
			MongoCollection<Document> collection_users=mongoDatabase.getCollection("users");
			MongoCollection<Document> collection_musics=mongoDatabase.getCollection("musics");
			Document document=new Document();
			
			User user=new User();
			user.setNikename("hhh");
			user.setNumberid("16203310161");
			user.setPassword("yyyyyy");

			
			Music music=new Music();
			music.setName("我们不一样");
			music.setSonger("dazhuang");
			music.setMd5Value("456a4df4sf56s4df6s");
			
			collection_users.find(eq("musicsheets", "dsfdsfsdfdsfdsfdsfs")).forEach(printBlock);;
			
			
//添加对象			
//			document.putAll(MongoUtil.Obj2Map(user));
//			collection.insertOne(document);
//			MongoUtil.insert(collection, user);
//			MongoUtil.insert(collection_musics, music);
			

			
//更新对象
//			List<String>  muStirngs=new ArrayList<String>();
//			muStirngs.add("dsfdsfsdfdsfdsfdsfs");
//			muStirngs.add("dsfdsfsdf4646446");
//			user.setMusicsheets(muStirngs);
//			user.set_id("4f43448ba48e4e5c9823cb5988fe5abb");
////			System.out.println(user);
//			document.append("$set",MongoUtil.Obj2Map(user));
			
//			Music m=MongoUtil.findOne(collection_musics, eq("_id","2f6068c592414589a63975577990c6e7"),new TypeReference<Music>() {} );
//			m.setSonger("ttttt");
//			m.setName("ssssss");
//			MongoUtil.update(collection_musics, m);
					
//			collection.updateOne(eq("_id","4f43448ba48e4e5c9823cb5988fe5abb"), document);
//查找对象
			
//			Document document2=collection_users.find(eq("_id","4f43448ba48e4e5c9823cb5988fe5abb")).first();
//			User user2=JSON.parseObject(document2.toJson(),new TypeReference<User>() {});
//			System.out.println(user2);
//			System.out.println(MongoUtil.findOne(collection, eq("_id", "37663933768e47f1b8228ca8391400d7")).toString());
//			Object object=MongoUtil.findOne(collection_musics, eq("_id","2f6068c592414589a63975577990c6e7"),new TypeReference<Music>() {} );
//			System.out.println(object==null?"yesy":object.toString());
			
			
//删除对象
//			collection.deleteOne(eq("_id","c948ab8dbc5649baaa1811f88c2d1da2"));
//			collection_users.find().forEach(printBlock);
//			collection_musics.find().forEach(printBlock);
//			User uuuu=MongoUtil.findOne(collection_users, eq("_id", "37663933768e47f1b8228ca8391400d7"), new TypeReference<User>() {});
			
//			MongoUtil.delete(collection_users, uuuu);
//			System.out.println(document.toJson());
			

	}
	 static Block<Document> printBlock = new Block<Document>() {
	        public void apply(final Document document) {
	            System.out.println(document.toJson());
	        }
	    };
}
