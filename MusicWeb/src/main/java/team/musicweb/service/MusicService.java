package team.musicweb.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.bson.Document;

import com.alibaba.fastjson.TypeReference;
import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.eq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import team.musicweb.moudle.Music;
import team.musicweb.moudle.MusicSheet;
import team.musicweb.util.MongoUtil;

public class MusicService {
	private MongoCollection<Document> collection_music;
	private MongoCollection<Document> collection_musicsheet;

	public MusicService() {
	}

	public MusicService(MongoCollection<Document> collection_music,MongoCollection<Document> collection_musicsheet) {
		this.collection_music = collection_music;
		this.collection_musicsheet=collection_musicsheet;
	}

	/**
	 * 判断服务器是否已经有这首歌曲
	 * 
	 * @param md5Value
	 * @return
	 */
	public boolean hasMusic(String md5Value) {

		return MongoUtil.findOne(collection_music, eq("md5Value", md5Value), new TypeReference<Music>() {
		}) == null ? false : true;
	}

	/**
	 * 删除歌单歌曲
	 * 
	 * @param request
	 * @return
	 */
	public boolean remove(HttpServletRequest request) {
		String musicsheetId = request.getParameter("MusicsheetId");
		String musicId = request.getParameter("MusicId");
		MusicSheet musicSheet = MongoUtil.findOne(collection_musicsheet, eq("_id", musicsheetId), new TypeReference<MusicSheet>() {
		});
		if (musicSheet != null && musicSheet.getMusics().remove(musicId))
			return MongoUtil.update(collection_musicsheet, musicSheet);
		else
			return false;
	}

	/**
	 * 通过MD5迁移歌曲到歌单
	 * 
	 * @param request
	 * @return
	 */
	public boolean transmusic(HttpServletRequest request) {
		String md5Value=request.getParameter("Md5");
		String musicSheetId=request.getParameter("MusicSheetId");
		Music music=MongoUtil.findOne(collection_music, eq("md5Value", md5Value), new TypeReference<Music>() {});
		MusicSheet musicSheet = MongoUtil.findOne(collection_musicsheet, eq("_id", musicSheetId), new TypeReference<MusicSheet>() {});
		if(music!=null && musicSheet!=null) {
				musicSheet.getMusics().add(music.get_id());
				return MongoUtil.update(collection_musicsheet, musicSheet);
		}
		return false;
	}

	/**
	 * 上传歌曲并且保存到歌单
	 * 
	 * @param request
	 * @return
	 */
	public boolean upload(HttpServletRequest request) {
		// request 中 url qurystirng should do url decode.
		//referance:http://blog.csdn.net/lian_zhihui1984/article/details/6822201
		// 判断enctype属性是否为multipart/form-data

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// 设置上传内容的大小限制（单位：字节）
		upload.setSizeMax(30*1024*1024);

		// Parse the request
		List<?> items=null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e1) {
			e1.printStackTrace();
		}

		
		String musicMd5="";
		String musicsheetId="";
		String musicName="";
		String musicSinger="";
		Iterator<?> iter = items.iterator();
		while (iter.hasNext()) {
		    FileItem item = (FileItem) iter.next();

		    if (item.isFormField()) {
		    	//如果是普通表单字段
		        String name = item.getFieldName();
				    String value="";
					try {
						value = item.getString("UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				    switch (name) {
					case "MusicSheetId":
						musicsheetId=value;
						break;
						case "MusicName":
							musicName=value;
						case "MusicSinger":
							musicSinger=value;
					}
		    } else {
		    	//如果是文件字段
//		        String fieldName = item.getFieldName();
//		        String value = item.getName();//会将完整路径名传过来  
//                int start = value.lastIndexOf("\\");  
//                String fileName = value.substring(start+1);
                OutputStream out=null;
                InputStream in=null;
				try {
					in = item.getInputStream();
					musicMd5=DigestUtils.md5Hex(IOUtils.toByteArray(in));
	    			ServletContext sct=request.getServletContext();
	    			String serverSavaPath=request.getServletContext().getRealPath("/")+sct.getInitParameter("MusicFilePath").toString();
	    			out=new FileOutputStream(new File(serverSavaPath,musicMd5+".mp3"));
	    			int length=0;
	    			byte[] buf=new byte[1024];
	    			in.close();
	    			//重新获取流，因为 hash操作，将流指针移到最后
	    			in = item.getInputStream();
	    			while((length=in.read(buf))!=-1) {
	    				out.write(buf, 0, length);
	    			}
				} catch (IOException e) {
					e.printStackTrace();
				} 
				finally {
					//close stream
						try {
							if(in!=null)
								in.close();
							if(out!=null)
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
				
		    }
		}

		//保存music
		Music music=new Music();
		music.setName(musicName);
		music.setSonger(musicSinger);
		music.setMd5Value(musicMd5);
		MongoUtil.insert(collection_music, music);
		
		//更新歌单
		MusicSheet musicSheet = MongoUtil.findOne(collection_musicsheet, eq("_id", musicsheetId), new TypeReference<MusicSheet>() {});
		musicSheet.getMusics().add(music.get_id());
		return MongoUtil.update(collection_musicsheet, musicSheet);
		
		
		
		
	/*//这些代码应该不能用了，还没测试
		String musicsheetId=request.getParameter("MusicSheetId");
		String musicName=request.getParameter("MusicName");
		String musicSinger=request.getParameter("MusicSinger");
		try {
			musicName=URLDecoder.decode(musicName, "utf-8");
			musicSinger=URLDecoder.decode(musicSinger,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取request form 中 MusicContent 流数据
		ServletInputStream servletInputStream=null;
		OutputStream out=null;
		try {
			servletInputStream=request.getInputStream();
			String musicMd5=DigestUtils.md5Hex(IOUtils.toByteArray(servletInputStream));
			ServletContext sct=request.getServletContext();
			String serverSavaPath=sct.getInitParameter("MusicFilePath").toString();
			out=new FileOutputStream(new File(serverSavaPath+musicName+".mp3"));
			int length=0;
			byte[] buf=new byte[1024];
			while((length=servletInputStream.read(buf))!=-1) {
				out.write(buf, 0, length);
			}
			//保存music
			Music music=new Music();
			music.setName(musicName);
			music.setSonger(musicSinger);
			music.setMd5Value(musicMd5);
			MongoUtil.insert(collection_music, music);
			
			//更新歌单
			MusicSheet musicSheet = MongoUtil.findOne(collection_musicsheet, eq("_id", musicsheetId), new TypeReference<MusicSheet>() {});
			musicSheet.getMusics().add(music.get_id());
			MongoUtil.update(collection_musicsheet, musicSheet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			//close stream
				try {
					if(servletInputStream!=null)
						servletInputStream.close();
					if(out!=null)
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return false;
		*/
	}

}
