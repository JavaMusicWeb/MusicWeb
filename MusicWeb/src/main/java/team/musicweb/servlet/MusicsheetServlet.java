package team.musicweb.servlet;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import team.musicweb.service.MusicsheetService;

/**
 * Servlet implementation class MusicsheetServlet
 */
public class MusicsheetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MongoClient mongoClient;
	private MongoDatabase mongoData;
	private MusicsheetService musicsheetService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MusicsheetServlet() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		mongoClient = new MongoClient();
		mongoData = mongoClient.getDatabase(this.getServletContext().getInitParameter("DatabaseName"));
		musicsheetService = new MusicsheetService(mongoData.getCollection(config.getInitParameter("MusicsheetCollection")),
				mongoData.getCollection(config.getInitParameter("UserCollection")),
				mongoData.getCollection(config.getInitParameter("MusicCollection")),
				mongoData.getCollection(config.getInitParameter("CommentCollection")));
		
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		mongoClient.close();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requesUrl=request.getRequestURI().toString();
		JSONObject resJson=new JSONObject();
		try {
			if(requesUrl.contains("getSongs")) {
				resJson.put("status", 200);
				resJson.put("data", musicsheetService.getMusics(request));
			}else if(requesUrl.contains("getAll")) {
				resJson.put("status", 200);
				int perSize=Integer.parseInt(this.getServletConfig().getInitParameter("PerSize"));
				resJson.put("total", musicsheetService.getMusicsheetCount());
				resJson.put("data", musicsheetService.getAllMusicsheets(request, perSize));
			}else if(requesUrl.contains("delete")) {
				resJson.put("status", musicsheetService.delete(request)?200:2004);
			}else if(requesUrl.contains("removeComment")) {
				resJson.put("status", musicsheetService.removeComment(request)?200:-1);
			}else if(requesUrl.contains("getComments")) {
				resJson.put("status", 200);
				resJson.put("data", musicsheetService.getComments(request));
			}else if(requesUrl.contains("getInfo")) {
				resJson.put("status", 200);
				resJson.put("data",musicsheetService.getMusicsheetsByUserid(request));
			}
		} catch (Exception e) {
			e.printStackTrace();
			resJson.put("status", 500);
		}
		finally {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().append(resJson.toJSONString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requesUrl=request.getRequestURI().toString();
		JSONObject resJson=new JSONObject();
		try {
			if(requesUrl.contains("publishComment")) {
				resJson.put("status", musicsheetService.publishComment(request)?200:-1);
			}else if(requesUrl.contains("create")) {
				resJson.put("status", musicsheetService.create(request)?200:-1);
			}
		} catch (Exception e) {
			resJson.put("status", 500);
		}
		finally {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().append(resJson.toString());
		}
	}

}
