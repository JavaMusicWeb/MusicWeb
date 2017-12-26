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

import team.musicweb.service.MusicService;

/**
 * Servlet implementation class MusicServlet
 */
public class MusicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MongoClient mongoClient;
	private MongoDatabase mongoData;
	private MusicService musicService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MusicServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// 必须加这个否则报错
		super.init(config);
		mongoClient = new MongoClient();
		mongoData = mongoClient.getDatabase(this.getServletContext().getInitParameter("DatabaseName"));
		musicService = new MusicService(mongoData.getCollection(config.getInitParameter("MusicCollection")),mongoData.getCollection(config.getInitParameter("MusicsheetCollection")));
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		mongoClient.close();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String requestUrl = request.getRequestURL().toString();
		JSONObject resJosn = new JSONObject();
		try {
			if (requestUrl.contains("thunder")) {
				resJosn.put("status", (musicService.hasMusic(request.getParameter("md5")) ? 1 : -1));
			} else if (requestUrl.contains("remove")) {
				resJosn.put("status", (musicService.remove(request) ? 1 : -1));
			}

		} catch (Exception e) {
			resJosn.put("status", 500);
		}
		finally {
			response.setContentType("application/json");
			response.getWriter().append(resJosn.toString());
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String requestUrl = request.getRequestURI().toString();
		JSONObject resJosn = new JSONObject();
		try {
			if (requestUrl.contains("transmusic")) {
				resJosn.put("status", (musicService.transmusic(request) ? 1 : -1));
			} else if (requestUrl.contains("upload")) {
				resJosn.put("status", (musicService.upload(request) ? 1 : -1));
			}
		} catch (Exception e) {
			resJosn.put("status", 500);
		} finally {
			response.setContentType("application/json");
			response.getWriter().append(resJosn.toString());
		}
	}

}
