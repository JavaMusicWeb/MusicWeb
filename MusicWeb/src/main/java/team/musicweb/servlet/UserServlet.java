package team.musicweb.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import team.musicweb.moudle.User;
import team.musicweb.service.UserService;
import team.musicweb.util.VerifyCodeUtil;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MongoClient mongoClient;
	private MongoDatabase mongoData;
	private UserService userService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// 大坑，必须初始化父类 init function
		super.init(config);

		mongoClient = new MongoClient();
		mongoData = mongoClient.getDatabase(this.getServletContext().getInitParameter("DatabaseName"));
		userService = new UserService(mongoData.getCollection(config.getInitParameter("UserCollection")));

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

		String requestUrl = request.getRequestURI();
		if (requestUrl.contains("getValidate")) {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			// 生成随机字串
			String verifyCode = VerifyCodeUtil.generateVerifyCode(4);
			System.out.println("validate:" + verifyCode);
			// 存入会话session
			HttpSession session = request.getSession(true);
			// 删除以前的
			session.removeAttribute("validate");
			session.setAttribute("validate", verifyCode.toLowerCase());
			// 生成图片
			int w = 100, h = 30;
			VerifyCodeUtil.outputImage(w, h, response.getOutputStream(), verifyCode);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject resJson = new JSONObject();
		String requestUrl = request.getRequestURI();

		try {
			if (requestUrl.contains("register")) {
				int status = userService.register(request);
				resJson.put("status", status);
				if (status == 200)
					resJson.put("msg", "successful!");
				else if(status==2001)
					resJson.put("msg", "NumberId has registered!");
				else if(status==20010)
					resJson.put("msg", "Input error!");
				else 
					resJson.put("msg", "Server error!");
			} else if (requestUrl.contains("login")) {
				// 验证验证码
				HttpSession session = request.getSession();
				String standardVerify = session.getAttribute("validate").toString();
				String userVerify = request.getParameter("Validate").toString().toLowerCase();
				if (standardVerify == null || !userVerify.equals(standardVerify)) {
					resJson.put("status", 2002);
					resJson.put("msg", "validate error!");
				} else {
					User user = userService.login(request);
					if (user == null) {
						resJson.put("status", 2003);
						resJson.put("msg", "numberid or password error!");
					} else {
						resJson.put("status", 200);
						resJson.put("msg", "successful!");
						//设置seesion
						session.setAttribute("UserNumber", user.getNumberid());
						Map<String, String> data = new HashMap<String, String>();
						data.put("NikeName", user.getNikename());
						data.put("NumberId", user.getNumberid());
						resJson.put("data", data);
					}
				}
			} else if (requestUrl.contains("editInfo")) {
				User user = userService.updateInfo(request);
				if (user == null) {
					resJson.put("status", 500);
					resJson.put("msg", "update error!");
				} else {
					resJson.put("status", 200);
					resJson.put("msg", "successful!");
					Map<String, String> data = new HashMap<String, String>();
					data.put("NikeName", user.getNikename());
					resJson.put("data", data);
				}
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

}
