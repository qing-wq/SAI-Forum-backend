package ink.whi.service.user.service.user;

import ink.whi.service.user.repo.dao.UserDao;
import ink.whi.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
}
