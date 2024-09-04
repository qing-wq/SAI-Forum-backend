package ink.whi.web.oauth;

import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.core.config.GithubClientProperties;
import ink.whi.core.utils.SessionUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Github登录接口
 * @author: qing
 * @Date: 2024/9/3
 */
@RestController
@RequestMapping(path = "oauth")
public class OAuthController {

    private static final Logger log = LoggerFactory.getLogger(OAuthController.class);
    @Autowired
    private GithubClientProperties githubClientProperties;

    /**
     * 跳转到授权页面
     * @param response
     * @throws IOException
     */
    @GetMapping(path = "github")
    public void auth(HttpServletResponse response) throws IOException {
        String authorizeUri = "https://github.com/login/oauth/authorize";
        String redirectUri = "http://localhost:8080/oauth/redirect";

        String url = authorizeUri
                + "?client_id=" + githubClientProperties.getClientId()
                + "&redirect_uri=" + redirectUri;
        response.sendRedirect(url);
    }

    /**
     * Github回调接口
     * @param requestToken 授权码
     * @return
     */
    @GetMapping(path = "redirect")
    public ResVo<String> handleRedirect(@RequestParam("code") String requestToken, HttpServletResponse response) {
        RestTemplate restTemplate = new RestTemplate();

        // 获取Token
        String tokenUrl = "https://github.com/login/oauth/access_token"
                + "?client_id=" + githubClientProperties.getClientId()
                + "&client_secret=" + githubClientProperties.getClientSecret()
                + "&code=" + requestToken;

        AccessTokenResponse tokenResponse = restTemplate.postForObject(tokenUrl, null, AccessTokenResponse.class);
        String accessToken = tokenResponse.getAccessToken();
        if (accessToken == null) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "授权码错误");
        }

        // 将token返回给客户端
        Cookie githubToken = SessionUtil.newCookie("github_token", accessToken);
        response.addCookie(githubToken);
        String userData = getUserInfo(accessToken);

        // TODO: 通过userData创建账户
        return ResVo.ok(userData);
    }

    public String getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        // 获取用户信息
        String apiUrl = "https://api.github.com/user";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> resp = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        return resp.getBody();
    }
}
