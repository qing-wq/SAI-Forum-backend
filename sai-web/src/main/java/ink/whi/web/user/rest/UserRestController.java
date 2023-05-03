package ink.whi.web.user.rest;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.FollowSelectEnum;
import ink.whi.api.model.enums.FollowTypeEnum;
import ink.whi.api.model.enums.HomeSelectEnum;
import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.TagSelectDTO;
import ink.whi.api.model.vo.user.dto.FollowUserInfoDTO;
import ink.whi.api.model.vo.user.dto.UserStatisticInfoDTO;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.user.repo.dao.UserRelationDao;
import ink.whi.service.user.service.UserRelationService;
import ink.whi.service.user.service.UserService;
import ink.whi.web.user.vo.UserHomeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 用户接口
 * @author: qing
 * @Date: 2023/5/1
 */
@RestController
@RequestMapping(path = "user")
public class UserRestController {
    private static final List<String> homeSelectTags = Arrays.asList("article", "read", "follow", "collection");
    private static final List<String> followSelectTags = Arrays.asList("follow", "fans");
    @Autowired
    private UserService userService;

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private UserRelationService userRelationService;

    /**
     * 用户主页接口
     * @param userId
     * @param homeSelectType 主页选择标签
     * @param followSelectType 关注列选择标签
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "/{userId}")
    public ResVo<UserHomeVo> getUserHome(@PathVariable(name = "userId") Long userId,
                             @RequestParam(name = "homeSelectType", required = false) String homeSelectType,
                             @RequestParam(name = "followSelectType", required = false) String followSelectType) {
        UserHomeVo vo = new UserHomeVo();
        vo.setHomeSelectType(StringUtils.isBlank(homeSelectType) ? HomeSelectEnum.ARTICLE.getCode() : homeSelectType);
        vo.setFollowSelectType(StringUtils.isBlank(followSelectType) ? FollowTypeEnum.FOLLOW.getCode() : followSelectType);

        UserStatisticInfoDTO userInfo = userService.queryUserInfoWithStatistic(userId);
        vo.setUserHome(userInfo);

        List<TagSelectDTO> homeSelectTags = homeSelectTags(vo.getHomeSelectType(), Objects.equals(userId, ReqInfoContext.getReqInfo().getUserId()));
        vo.setHomeSelectTags(homeSelectTags);

        userHomeSelectList(vo, userId);
        return ResVo.ok(vo);
    }

    /**
     * Home页选择列表标签
     *
     * @param selectType
     * @param isAuthor true 表示当前为查看自己的个人主页
     * @return
     */
    private List<TagSelectDTO> homeSelectTags(String selectType, boolean isAuthor) {
        List<TagSelectDTO> tags = new ArrayList<>();
        homeSelectTags.forEach(tag -> {
            if (!isAuthor && "read".equals(tag)) {
                // 只有本人才能看自己的阅读历史
                return;
            }
            TagSelectDTO tagSelectDTO = new TagSelectDTO();
            tagSelectDTO.setSelectType(tag);
            tagSelectDTO.setSelectDesc(HomeSelectEnum.fromCode(tag).getDesc());
            tagSelectDTO.setSelected(selectType.equals(tag));
            tags.add(tagSelectDTO);
        });
        return tags;
    }

    /**
     * 返回关注用户选择列表标签
     *
     * @param selectType
     * @return
     */
    private List<TagSelectDTO> followSelectTags(String selectType) {
        List<TagSelectDTO> tags = new ArrayList<>();
        followSelectTags.forEach(tag -> {
            TagSelectDTO tagSelectDTO = new TagSelectDTO();
            tagSelectDTO.setSelectType(tag);
            tagSelectDTO.setSelectDesc(FollowSelectEnum.fromCode(tag).getDesc());
            tagSelectDTO.setSelected(selectType.equals(tag));
            tags.add(tagSelectDTO);
        });
        return tags;
    }

    /**
     * 返回选择列表
     *
     * @param vo
     * @param userId
     */
    private void userHomeSelectList(UserHomeVo vo, Long userId) {
        PageParam pageParam = PageParam.newPageInstance();
        HomeSelectEnum select = HomeSelectEnum.fromCode(vo.getHomeSelectType());
        if (select == null) {
            return;
        }

        switch (select) {
            case ARTICLE:
            case READ:
            case COLLECTION:
                PageListVo<ArticleDTO> dto = articleReadService.queryArticlesByUserAndType(userId, pageParam, select);
                vo.setHomeSelectList(dto);
                return;
            case FOLLOW:
                // 关注用户与被关注用户
                // 获取选择标签
                List<TagSelectDTO> followSelectTags = followSelectTags(vo.getFollowSelectType());
                vo.setFollowSelectTags(followSelectTags);
                initFollowFansList(vo, userId, pageParam);
                return;
            default:
        }
    }

    private void initFollowFansList(UserHomeVo vo, Long userId, PageParam pageParam) {
        PageListVo<FollowUserInfoDTO> followList;
        boolean needUpdateRelation = false;
        if (vo.getFollowSelectType().equals(FollowTypeEnum.FOLLOW.getCode())) {
            followList = userRelationService.getUserFollowList(userId, pageParam);
        } else {
            // 查询粉丝列表时，只能确定粉丝关注了userId，但是不能反向判断，因此需要再更新下映射关系，判断userId是否有关注这个用户
            followList = userRelationService.getUserFansList(userId, pageParam);
            needUpdateRelation = true;
        }

        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        if (!Objects.equals(loginUserId, userId) || needUpdateRelation) {
            userRelationService.updateUserFollowRelationId(followList, loginUserId);
        }
        vo.setFollowList(followList);
    }
}
