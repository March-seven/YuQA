package com.ycy.YuQA.model.dto.question;

import lombok.Data;

/**
 * 用于 AI 评分
 */
@Data
public class QuestionAnswerDTO {

    /**
     * 题目
     */
    private String title;

    /**
     * 用户答案
     */
    private String userAnswer;
}