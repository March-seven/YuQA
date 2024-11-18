package com.ycy.YuQA.scoring;

import com.ycy.YuQA.model.entity.App;
import com.ycy.YuQA.model.entity.UserAnswer;

import java.util.List;

/**
 * 评分策略接口
 */

public interface ScoringStrategy {

    /**
     * 执行评分
     * @param choice
     * @param app
     * @return
     * @throws Exception
     */
    UserAnswer doScore(List<String> choice, App app) throws Exception;
}
