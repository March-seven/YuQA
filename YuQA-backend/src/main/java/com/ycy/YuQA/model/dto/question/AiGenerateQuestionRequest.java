package com.ycy.YuQA.model.dto.question;

import lombok.Data;

import java.io.Serializable;

@Data
public class AiGenerateQuestionRequest implements Serializable {

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 题目数量
     */
    int questionNumber = 10;

    /**
     * 选项数量
     */
    int optionNumber = 2;

    private static final long serialVersionUID = 1L;
}