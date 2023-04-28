package ink.whi.web.hook.filter;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.core.utils.CrossUtil;
import ink.whi.core.utils.IpUtil;
import ink.whi.service.statistics.service.StatisticSettingService;
import ink.whi.web.global.GlobalInitService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * 鉴权Filter
 * @author: qing
 * @Date: 2023/4/27
 */
@WebFilter(urlPatterns = "/*", filterName = "authFilter", asyncSupported = true)
public class AuthFilter implements Filter {
    private static Logger REQ_LOG = LoggerFactory.getLogger("req");

    @Autowired
    private StatisticSettingService statisticSettingService;

    @Autowired
    private GlobalInitService globalInitService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest req = null;
        try {
            req = initReqInfo((HttpServletRequest) request);
            CrossUtil.buildCors(req, (HttpServletResponse) response);
            chain.doFilter(req, response);
        }finally {
            buildRequestLog(ReqInfoContext.getReqInfo(), req, System.currentTimeMillis() - start);
            ReqInfoContext.clear();
        }
    }

    private HttpServletRequest initReqInfo(HttpServletRequest request) {
        if (staticURI(request)) {
            // 静态资源直接放行
            return request;
        }

        ReqInfoContext.ReqInfo reqInfo = new ReqInfoContext.ReqInfo();
        reqInfo.setHost(request.getHeader("host"));
        reqInfo.setPath(request.getPathInfo());
        reqInfo.setReferer(request.getHeader("referer"));
        reqInfo.setClientIp(IpUtil.getClientIp(request));
        reqInfo.setUserAgent(request.getHeader("User-Agent"));
        request = this.wrapperRequest(request, reqInfo);
        // 校验token
        globalInitService.initLoginUser(reqInfo);
        ReqInfoContext.addReqInfo(reqInfo);
        return request;
    }

    private HttpServletRequest wrapperRequest(HttpServletRequest request, ReqInfoContext.ReqInfo reqInfo) {
        BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
        reqInfo.setPayload(requestWrapper.getBodyString());
        return requestWrapper;
    }

    /**
     * 日志输出
     * @param req
     * @param request
     * @param costTime
     */
    private void buildRequestLog(ReqInfoContext.ReqInfo req, HttpServletRequest request, long costTime) {
        if (req == null || staticURI(request)) {
            return;
        }

        StringBuilder msg = new StringBuilder();
        msg.append("method=").append(request.getMethod()).append("; ");
        if (StringUtils.isNotBlank(req.getReferer())) {
            msg.append("referer=").append(URLDecoder.decode(req.getReferer())).append("; ");
        }
        msg.append("remoteIp=").append(req.getClientIp());
        msg.append("; agent=").append(req.getUserAgent());

        if (req.getUserId() != null) {
            // 打印用户信息
            msg.append("; user=").append(req.getUserId());
        }

        msg.append("; uri=").append(request.getRequestURI());
        if (StringUtils.isNotBlank(request.getQueryString())) {
            msg.append('?').append(URLDecoder.decode(request.getQueryString()));
        }

        msg.append("; payload=").append(req.getPayload());
        msg.append("; cost=").append(costTime);
        REQ_LOG.info("{}", msg);

        // todo: 保存请求计数
        statisticSettingService.saveRequestCount(req.getClientIp());
    }

    private boolean staticURI(HttpServletRequest request) {
        return request == null
                || request.getRequestURI().endsWith("css")
                || request.getRequestURI().endsWith("js")
                || request.getRequestURI().endsWith("png")
                || request.getRequestURI().endsWith("ico")
                || request.getRequestURI().endsWith("svg");
    }
}
