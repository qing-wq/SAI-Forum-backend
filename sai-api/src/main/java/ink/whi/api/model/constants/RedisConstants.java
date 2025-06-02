package ink.whi.api.model.constants;

/**
 * @author: qing
 * @Date: 2024/10/19
 */
public class RedisConstants {

    public static final long ONE_MOMENT = 60L;
    public static final long ONE_HOUR = 60L * ONE_MOMENT;
    public static final long ONE_DAY = 24L * ONE_HOUR;

    /**
     * 全局前缀
     */
    public static final String REDIS_PAI = "sai:";

    /**
     * 分布式锁
     */
    public static final String REDIS_LOCK = "lock:";

    /**
     *=================article==================
     */
    public static final String REDIS_PRE_ARTICLE = "article:";

    public static final String ARTICLE_LIST_HOME_CACHE_KEY = "article_list_home_";

    /**
     *=================rank==================
     */
    public static final String USER_RANK_KEY = "rank:user";

    /**
     *=================count==================
     */
    public static final String REDIS_PRE_COUNT = "count:";

    public static final String REDIS_PRE_ARTICLE_PRAISE = "praise:article_";
    public static final String REDIS_PRE_ARTICLE_COLLECT = "collect:article_";
    public static final String REDIS_PRE_COMMENT_PRAISE = "praise:comment_";
    public static final String REDIS_PRE_COMMENT_COLLECT = "collect:comment_";
    
}
