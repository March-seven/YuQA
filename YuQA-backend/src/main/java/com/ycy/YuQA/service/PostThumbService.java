package com.ycy.YuQA.service;

import com.ycy.YuQA.model.entity.PostThumb;
import com.ycy.YuQA.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 帖子点赞服务
 *
 * @author <a href="https://github.com/liycy">程序员ycy</a>
 * @from <a href="https://ycy.icu">编程导航知识星球</a>
 */
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostThumb(long postId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doPostThumbInner(long userId, long postId);
}
