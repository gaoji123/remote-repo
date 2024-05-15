package com.gcf.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcf.constants.SystemConstants;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.entity.Comment;
import com.gcf.domain.vo.CommentVo;
import com.gcf.domain.vo.PageVo;
import com.gcf.enums.AppHttpCodeEnum;
import com.gcf.exception.SystemException;
import com.gcf.mapper.CommentMapper;
import com.gcf.service.CommentService;
import com.gcf.service.UserService;
import com.gcf.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;


    @Override
    public ResponseResult commentList(String commentType ,Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId, articleId);
        queryWrapper.orderByDesc(Comment::getCreateTime);
        //根评论 rootId为-1
        queryWrapper.eq(Comment::getRootId, -1);
        queryWrapper.eq(Comment::getType,commentType);

        //分页查询
        Page<Comment> page = new Page(pageNum, pageSize);
        page(page, queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());
        for (CommentVo commentVo : commentVoList) {
            List<CommentVo> commentVos = getChildren(commentVo.getId());
            commentVo.setChildren(commentVos);
        }

        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }


    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }


    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Comment::getRootId, id);
        List<Comment> list = list(queryWrapper);
        List<CommentVo> commentVoList = toCommentVoList(list);
        return commentVoList;
    }

    private List<CommentVo> toCommentVoList(List<Comment> list) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合
        for (CommentVo commentVo : commentVos) {
            //通过creatyBy查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            if (commentVo.getToCommentUserId() != -1) {
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }
}