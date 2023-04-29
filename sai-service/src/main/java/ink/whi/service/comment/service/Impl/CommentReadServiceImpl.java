package ink.whi.service.comment.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.enums.PraiseStatEnum;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.comment.dto.BaseCommentDTO;
import ink.whi.api.model.vo.comment.dto.SubCommentDTO;
import ink.whi.api.model.vo.comment.dto.TopCommentDTO;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.service.comment.converter.CommentConverter;
import ink.whi.service.comment.repo.dao.CommentDao;
import ink.whi.service.comment.repo.entity.CommentDO;
import ink.whi.service.comment.service.CommentReadService;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.service.CountService;
import ink.whi.service.user.service.UserFootService;
import ink.whi.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
@Service
public class CommentReadServiceImpl implements CommentReadService {
    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserService userService;

    @Autowired
    private CountService countService;

    @Autowired
    private UserFootService userFootService;

    @Override
    public Integer queryCommentCount(Long articleId) {
        return commentDao.countCommentByArticleId(articleId);
    }

    @Override
    public List<TopCommentDTO> getArticleComments(Long articleId, PageParam page) {
        // 1.查询一级评论
        List<CommentDO> comments = commentDao.listTopCommentList(articleId, page);
        if (CollectionUtils.isEmpty(comments)) {
            return Collections.emptyList();
        }
        // map 存 commentId -> 评论
        Map<Long, TopCommentDTO> topComments = comments.stream().collect(Collectors.toMap(CommentDO::getId, CommentConverter::toTopDto));

        // 2.查询非一级评论
        List<CommentDO> subComments = commentDao.listSubCommentIdMappers(articleId, topComments.keySet());

        // 3.构建一级评论的子评论
        buildCommentRelation(subComments, topComments);

        // 4.挑出需要返回的数据，排序，并补齐对应的用户信息，最后排序返回
        List<TopCommentDTO> result = new ArrayList<>();
        comments.forEach(comment -> {
            TopCommentDTO dto = topComments.get(comment.getId());
            fillTopCommentInfo(dto);
            result.add(dto);
        });

        // 返回结果根据时间进行排序
        Collections.sort(result);
        return result;
    }

    /**
     * 构建父子评论关系
     */
    private void buildCommentRelation(List<CommentDO> subComments, Map<Long, TopCommentDTO> topComments) {
        Map<Long, SubCommentDTO> subCommentMap = subComments.stream().collect(Collectors.toMap(CommentDO::getId, CommentConverter::toSubDto));
        subComments.forEach(comment -> {
            TopCommentDTO top = topComments.get(comment.getTopCommentId());
            if (top == null) {
                return;
            }
            SubCommentDTO sub = subCommentMap.get(comment.getId());
            top.getChildComments().add(sub);
            if (Objects.equals(comment.getTopCommentId(), comment.getParentCommentId())) {
                return;
            }

            SubCommentDTO parent = subCommentMap.get(comment.getParentCommentId());
            sub.setParentContent(parent == null ? "~~该评论已删除~~" : parent.getCommentContent());
        });
    }

    /**
     * 填充评论对应的信息
     *
     * @param comment
     */
    private void fillTopCommentInfo(TopCommentDTO comment) {
        fillCommentInfo(comment);
        comment.getChildComments().forEach(this::fillCommentInfo);
        Collections.sort(comment.getChildComments());
    }

    /**
     * 填充评论对应的信息，如用户信息，点赞数等
     *
     * @param comment
     */
    private void fillCommentInfo(BaseCommentDTO comment) {
        BaseUserInfoDTO userInfoDO = userService.queryBasicUserInfo(comment.getUserId());
        if (userInfoDO == null) {
            // 如果用户注销，给一个默认的用户
            comment.setUserName("账号已注销");
            comment.setUserPhoto("");
            if (comment instanceof TopCommentDTO) {
                ((TopCommentDTO) comment).setCommentCount(0);
            }
        } else {
            comment.setUserName(userInfoDO.getUserName());
            comment.setUserPhoto(userInfoDO.getPhoto());
            if (comment instanceof TopCommentDTO) {
                ((TopCommentDTO) comment).setCommentCount(((TopCommentDTO) comment).getChildComments().size());
            }
        }

        // 点赞数
        Integer praiseCount = countService.queryCommentPraiseCount(comment.getCommentId());
        comment.setPraiseCount(praiseCount);

        // 查询当前登录用于是否点赞过
        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        if (loginUserId != null) {
            // 判断当前用户是否点过赞
            UserFootDO foot = userFootService.queryUserFoot(comment.getCommentId(), DocumentTypeEnum.COMMENT.getCode(), loginUserId);
            comment.setPraised(foot != null && Objects.equals(foot.getPraiseStat(), PraiseStatEnum.PRAISE.getCode()));
        } else {
            comment.setPraised(false);
        }
    }
}
