package com.own.bb.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.own.bb.common.CheckResult;
import com.own.bb.common.LoginParam;
import com.own.bb.common.SystemConstant;
import com.own.bb.utils.JwtUtils;

/**
 * 拦截器 用户权限校验
 */
public class SysInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(SysInterceptor.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			String JWT = (String) request.getSession().getAttribute("JWT");
			// String authHeader = request.getHeader("token");
			if (StringUtils.isEmpty(JWT)) {
				logger.debug("验证失败,签名验证不存在");
				response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":"
						+ request.getServerPort() + request.getContextPath() + "/" + LoginParam.LOGIN_URL);
				return false;
			} else {
				// 验证JWT的签名，返回CheckResult对象
				CheckResult checkResult = JwtUtils.validateJWT(JWT);
				if (checkResult.isSuccess()) {
					return true;
				} else {
					switch (checkResult.getErrCode()) {
					// 签名验证不通过
					case SystemConstant.JWT_ERRCODE_FAIL:
						logger.debug("签名验证不通过");
						break;
					// 签名过期，返回过期提示码
					case SystemConstant.JWT_ERRCODE_EXPIRE:
						logger.debug("签名过期,重新生成签名");
						String username = (String) request.getSession().getAttribute("username");
						JWT = JwtUtils.createJWT("1", username, SystemConstant.JWT_TTL);
						request.getSession().setAttribute("JWT", JWT);
						return true;
					default:
						break;
					}
					response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":"
							+ request.getServerPort() + request.getContextPath() + "/" + LoginParam.LOGIN_URL);
					return false;
				}
			}
		} else {
			return true;
		}
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (response.getStatus() == 500) {
			modelAndView.setViewName("/error/500");
		} else if (response.getStatus() == 404) {
			modelAndView.setViewName("/error/404");
		}
	}

	/**
	 * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。该方法将在整个请求完成之后，也就是DispatcherServlet渲染了视图执行，
	 * 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}