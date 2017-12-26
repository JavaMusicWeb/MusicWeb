package team.musicweb.service;

import static com.mongodb.client.model.Filters.eq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import team.musicweb.moudle.Comment;
import team.musicweb.moudle.Music;
import team.musicweb.moudle.MusicSheet;
import team.musicweb.moudle.User;
import team.musicweb.util.MongoUtil;

public class MusicsheetService {
	private MongoCollection<Document> collection_musicsheet;
	private MongoCollection<Document> collection_user;
	private MongoCollection<Document> collection_music;
	private MongoCollection<Document> collection_comment;

	public MusicsheetService() {
	}

	public MusicsheetService(MongoCollection<Document> collection_musicsheet, MongoCollection<Document> collection_user,
			MongoCollection<Document> collection_music, MongoCollection<Document> collection_comment) {
		this.collection_musicsheet = collection_musicsheet;
		this.collection_user = collection_user;
		this.collection_music = collection_music;
		this.collection_comment = collection_comment;
	}

	/**
	 * 创建歌单
	 * 
	 * @param request
	 * @return
	 */
	public boolean create(HttpServletRequest request) {
		// querystring should do url decode!
		// request 中 url qurystirng should do url decode.
		// referance:http://blog.csdn.net/lian_zhihui1984/article/details/6822201
		// http://blog.csdn.net/a352193394/article/details/7477041
		// 判断enctype属性是否为multipart/form-data

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// 设置上传内容的大小限制（单位：字节）
		upload.setSizeMax(5 * 1024 * 1024);

		// Parse the request
		List<?> items = null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e1) {
			e1.printStackTrace();
		}

		String picMd5 = "";
		String numberId = "";
		String sheetName = "";
		String picUrl = "";
		Iterator<?> iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();

			if (item.isFormField()) {
				// 如果是普通表单字段
				String name = item.getFieldName();
				String value = "";
				try {
					value = item.getString("UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				switch (name) {
				case "NumberId":
					numberId = value;
					break;
				case "SheetName":
					sheetName = value;
				default:
					return false;
				}
			} else {
				// 如果是文件字段
				// String fieldName = item.getFieldName();
				String value = item.getName();// 会将完整路径名传过来
				String suffix = value.substring(value.lastIndexOf(".") + 1);
				OutputStream out = null;
				InputStream in = null;
				try {
					in = item.getInputStream();
					picMd5 = DigestUtils.md5Hex(IOUtils.toByteArray(in));
					ServletContext sct = request.getServletContext();
					String serverSavaPath = sct.getInitParameter("PictureFilePath").toString();
					picUrl = picMd5 + "." + suffix;
					out = new FileOutputStream(new File(serverSavaPath, picUrl));
					int length = 0;
					byte[] buf = new byte[1024];
					while ((length = in.read(buf)) != -1) {
						out.write(buf, 0, length);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					// close stream
					try {
						if (in != null)
							in.close();
						if (out != null)
							out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}

		// 创建歌单
		MusicSheet musicSheet = new MusicSheet();
		musicSheet.setName(sheetName);
		musicSheet.setPicUrl(picUrl);
		MongoUtil.insert(collection_musicsheet, musicSheet);

		// 更新用户歌单关联
		User user = MongoUtil.findOne(collection_user, eq("numberid", numberId), new TypeReference<User>() {
		});
		if (user != null) {
			user.getMusicsheets().add(musicSheet.get_id());
			return MongoUtil.update(collection_user, user);
		}
		return false;

	}

	/**
	 * 删除歌单
	 * 
	 * @param HttpServletRequest
	 * @return
	 */
	public boolean delete(HttpServletRequest request) {
		String musicsheetId = request.getParameter("id");
		MusicSheet musicSheet = MongoUtil.findOne(collection_user, eq("numberid", musicsheetId),
				new TypeReference<MusicSheet>() {
				});
		if (musicSheet == null)
			return false;
		else
			return MongoUtil.delete(collection_musicsheet, musicSheet);
	}

	/**
	 * 获取 歌单集合大小
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	public int getMusicsheetCount() {
		int total = 0;
		for (Document doc : collection_musicsheet.find())
			total++;
		return total;
	}

	/***
	 * 获取某一页歌单
	 * 
	 * @param currentPage
	 * @param perSize
	 * @return 前端规定格式
	 */
	public List<MusicSheetPack> getAllMusicsheets(HttpServletRequest request, int perSize) {
		int currtenPage = Integer.parseInt(request.getParameter("current"));
		List<MusicSheetPack> ansls = new LinkedList<MusicSheetPack>();

		if (currtenPage < 0)
			return ansls;
		MusicSheet ms = null;
		MusicSheetPack msp = new MusicSheetPack();
		FindIterable<Document> col = collection_musicsheet.find().skip(currtenPage * perSize);
		try {
			for (Document doc : col) {
				ms = JSON.parseObject(doc.toJson(), new TypeReference<MusicSheet>() {
				});
				msp.setId(ms.get_id());
				msp.setName(ms.getName());
				msp.setPictureUrl(ms.getPicUrl());
				msp.setCreateTime(ms.getCreateTime());
				msp.setTotalSongs(String.valueOf(ms.getMusics().size()));
				ansls.add(msp);
			}
			return ansls;
		} catch (Exception e) {
			// TODO: handle exception
			return ansls;
		}
	}

	/***
	 * 获取某人的所有歌单
	 * 
	 * @param userid
	 * @return
	 */
	public List<MusicSheetPack> getMusicsheetsByUserid(HttpServletRequest request) {
		String numberId = request.getParameter("userid");
		List<MusicSheetPack> ansls = new LinkedList<MusicSheetPack>();
		User user = MongoUtil.findOne(collection_user, eq("numberid", numberId), new TypeReference<User>() {
		});
		if (user != null) {
			MusicSheet ms = null;
			MusicSheetPack msp = null;
			List<String> msids = user.getMusicsheets();
			for (String string : msids) {
				ms = MongoUtil.findOne(collection_musicsheet, eq("_id", string), new TypeReference<MusicSheet>() {
				});
				msp = new MusicSheetPack();
				msp.setId(ms.get_id());
				msp.setName(ms.getName());
				msp.setCreateTime(ms.getCreateTime());
				msp.setPictureUrl(ms.getPicUrl());
				msp.setTotalSongs(String.valueOf(ms.getMusics().size()));
				ansls.add(msp);
			}
		}
		return ansls;
	}

	/***
	 * 获取歌单的歌曲
	 * 
	 * @param musicsheetid
	 * @return
	 */
	public List<Music> getMusics(HttpServletRequest request) {
		List<Music> anList = new LinkedList<Music>();
		String msid = request.getParameter("id");
		MusicSheet ms = MongoUtil.findOne(collection_musicsheet, eq("_id", msid), new TypeReference<MusicSheet>() {
		});
		if (ms != null) {
			List<String> mids = ms.getMusics();
			for (String string : mids) {
				anList.add(MongoUtil.findOne(collection_music, eq("_id", string), new TypeReference<Music>() {
				}));
			}
		}
		return anList;
	}

	/**
	 * 评论歌单
	 * 
	 * @param request
	 * @return
	 */
	public boolean publishComment(HttpServletRequest request) {
		String userNum = "";
		try {
			HttpSession session = request.getSession(false);
			userNum = session.getAttribute("UserNumber").toString();
		} catch (Exception e) {
			return false;
		}
		String msId = request.getParameter("MusicsheetId");
		String context = request.getParameter("MusicsheetId");

		// 创建一条评论
		Comment comment = new Comment();
		comment.setContent(context);
		comment.setMusicshetId(msId);
		comment.setUserId(userNum);
		MongoUtil.insert(collection_comment, comment);

		// 建立和音乐单关系
		MusicSheet ms = MongoUtil.findOne(collection_musicsheet, eq("_id", msId), new TypeReference<MusicSheet>() {
		});
		if (ms != null) {
			ms.getComments().add(comment.get_id());
			return MongoUtil.update(collection_musicsheet, ms);
		} else
			return false;
	}

	/***
	 * 获取歌单评论
	 * 
	 * @param musicsheetid
	 * @return
	 */
	public List<MusicSheetComment> getComments(HttpServletRequest request) {
		List<MusicSheetComment> anList = new LinkedList<MusicSheetComment>();
		String msId = request.getParameter("MusicSheetId");
		MusicSheet ms = MongoUtil.findOne(collection_musicsheet, eq("_id", msId), new TypeReference<MusicSheet>() {
		});
		Comment c = null;
		MusicSheetComment msc = null;
		User user = null;
		if (ms != null) {
			List<String> commentIds = ms.getComments();
			for (String string : commentIds) {
				c = MongoUtil.findOne(collection_comment, eq("_id", string), new TypeReference<Comment>() {
				});
				user = MongoUtil.findOne(collection_user, eq("numberid", c.getUserId()), new TypeReference<User>() {
				});
				msc = new MusicSheetComment();
				msc.setId(c.get_id());
				msc.setCommentorId(user.getNumberid());
				msc.setCommentorNickName(user.getNikename());
				msc.setContent(c.getContent());
				msc.setTime(c.getPublishDate());
				anList.add(msc);
			}
		}
		return anList;
	}

	/**
	 * 删除评论
	 * 
	 * @param request
	 * @return
	 */
	public boolean removeComment(HttpServletRequest request) {
		String commentId = request.getParameter("id");
		Comment comment = MongoUtil.findOne(collection_comment, eq("_id", commentId), new TypeReference<Comment>() {
		});
		if (comment != null) {
			 MongoUtil.delete(collection_comment, comment);
			//删除歌单里面记录评论的列表
			 MusicSheet ms=MongoUtil.findOne(collection_musicsheet, eq("comments", comment.get_id()), new TypeReference<MusicSheet>() {});
			 ms.getComments().remove(comment.get_id());
			 return MongoUtil.update(collection_comment, ms);
		}
		else
			return false;
	}

	class MusicSheetComment {
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

	class MusicSheetPack {
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
