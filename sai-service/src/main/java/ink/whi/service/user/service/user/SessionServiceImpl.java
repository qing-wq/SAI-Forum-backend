package ink.whi.service.user.service.user;

import ink.whi.service.user.repo.dao.UserDao;
import ink.whi.service.user.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
@Component
public class SessionServiceImpl implements SessionService {

    @Autowired
    private UserDao userDao;

}
