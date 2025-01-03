package com.ycy.YuQA.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ycy.YuQA.model.dto.question.QuestionContentDTO;
import com.ycy.YuQA.model.entity.App;
import com.ycy.YuQA.model.entity.Question;
import com.ycy.YuQA.model.entity.ScoringResult;
import com.ycy.YuQA.model.entity.UserAnswer;
import com.ycy.YuQA.model.vo.QuestionVO;
import com.ycy.YuQA.service.AppService;
import com.ycy.YuQA.service.QuestionService;
import com.ycy.YuQA.service.ScoringResultService;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义测评类应用评分策略
 */
@ScoringStrategyConfig(appType = 1,scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;
    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        // 1. 根据 id 查询到题目和题目结果信息
        Long appId = app.getId();
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class).eq(Question::getId,appId)
        );
        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, appId)
        );


        // 2. 统计用户每个选择对应的属性个数，如 I = 10 个， E = 5 个
        // 初始化一个Map，用于存储每个选项的计数
        Map<String, Integer> optionCount = new HashMap<>();

        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();

        // 遍历题目列表
        for (QuestionContentDTO questionContentDTO : questionContent) {
            // 遍历答案列表
            for (String answer : choices) {
                // 遍历选项列表
                for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                    // 如果答案与选项的key匹配
                    if (option.getKey().equals(answer)) {
                        // 获取选项的result属性
                        String result = option.getResult();

                        // 如果result属性不在optionCount中，初始化为0
                        if (!optionCount.containsKey(result)) {
                            optionCount.put(result, 0);
                        }

                        // 在optionCount中增加计数
                        optionCount.put(result, optionCount.get(result) + 1);
                    }
                }
            }
        }


        // 3. 遍历每种评分结果，计算哪个结果的得分更高
        // 初始化最大得分和最大得分对应的评分结果
        int maxScore = 0;
        ScoringResult maxScoriongResult = scoringResultList.get(0);

        //遍历评分结果列表
        for (ScoringResult scoringResult : scoringResultList) {
            List<String> resultProp = JSONUtil.toList(scoringResult.getResultProp(), String.class);
            //计算当前评分结果的得分，[I,E] => [10,5] =>15
            int score = resultProp.stream()
                    .mapToInt(prop -> optionCount.getOrDefault(prop, 0))
                    .sum();
            //如果当前得分大于最大得分，更新最大得分和最大得分对应的评分结果
            if (score > maxScore) {
                maxScore = score;
                maxScoriongResult = scoringResult;
            }
        }

        // 4. 构造返回值，填充答案对象的属性
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(maxScoriongResult.getId());
        userAnswer.setResultName(maxScoriongResult.getResultName());
        userAnswer.setResultDesc(maxScoriongResult.getResultDesc());
        userAnswer.setResultPicture(maxScoriongResult.getResultPicture());
        
        return userAnswer;

        // 3. 遍历每种评分结果，计算哪个结果的得分更高

    }
}
