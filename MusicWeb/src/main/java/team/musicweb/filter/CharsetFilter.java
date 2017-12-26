package team.musicweb.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Servlet Filter implementation class CharsetFilter
 */
//@WebFilter("/CharsetFilter")
public class CharsetFilter implements Filter {
	private String encoding;

    /**
     * Default constructor. 
     */
    public CharsetFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		//set request charset UTF-8, this method must should be done before call doFilter.
		request.setCharacterEncoding(encoding);
		chain.doFilter(request, response);
		
		//set response charset UTF-8, this method must shoule be done after call doFilter.
		response.setCharacterEncoding(encoding);
//		System.out.println("test filter ecoding:"+response.getCharacterEncoding());
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		encoding=fConfig.getInitParameter("Encoding");
		
	}

}
