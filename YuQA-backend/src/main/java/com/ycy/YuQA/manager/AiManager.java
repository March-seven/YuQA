package com.ycy.YuQA.manager;

import com.ycy.YuQA.common.ErrorCode;
import com.ycy.YuQA.exception.BusinessException;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用AI请求管理器（智谱AI）
 */
@Component
public class AiManager {

    @Resource
    private ClientV4 clientV4;

    //稳定随机数
    private static final float STABLE_TEMPERATURE = 0.08f;


    //不稳定随机数
    private static final float UNSTABLE_TEMPERATURE = 0.95f;


    /**
     * 同步请求(答案较稳定）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doSyncStableRequest(String systemMessage,String userMessage){
        return doRequest(systemMessage,userMessage,Boolean.FALSE,STABLE_TEMPERATURE);
    }

    /**
     * 同步请求(答案较不稳定）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doSyncUnstableRequest(String systemMessage,String userMessage){
        return doRequest(systemMessage,userMessage,Boolean.FALSE,UNSTABLE_TEMPERATURE);
    }

    /**
     * 同步请求
     *
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    public String doSyncRequest(String systemMessage,String userMessage,Float temperature){
        return doRequest(systemMessage,userMessage,Boolean.FALSE,temperature);
    }

    /**
     * 通用请求(简化消息传递)
     *
     * @param systemMessage
     * @param userMessage
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(String systemMessage,String userMessage,Boolean stream,Float temperature){
        List<ChatMessage> chatMessageList = new ArrayList<>();
        ChatMessage SystemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(),systemMessage);
        chatMessageList.add(SystemChatMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(),userMessage);
        chatMessageList.add(userChatMessage);
        return doRequest(chatMessageList,stream,temperature);
    }


    /**
     * 通用请求
     *
     * @param messages
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(List<ChatMessage> messages,Boolean stream,Float temperature){
        //test
//        List<ChatMessage> messages1 = new ArrayList<>();
//        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "作为一名营销专家，请为智谱开放平台创作一个吸引人的slogan");
//        messages1.add(chatMessage);
        // 构造请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(stream)
                .temperature(temperature)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        try {
            ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
            System.out.println("model output:" + invokeModelApiResp.getData().getChoices().get(0));
            return invokeModelApiResp.getData().getChoices().get(0).toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,e.getMessage());
        }

    }


    /* 流式请求*/

    /**
     * 通用流式请求（简化消息传递）
     *
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    public Flowable<ModelData> doStreamRequest(String systemMessage, String userMessage, Float temperature) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        chatMessageList.add(systemChatMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        chatMessageList.add(userChatMessage);
        return doStreamRequest(chatMessageList, temperature);
    }


    /**
     * 通用流式请求
     *
     * @param messages
     * @param temperature
     * @return
     */
    public Flowable<ModelData> doStreamRequest(List<ChatMessage> messages, Float temperature) {
        // 构建请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.TRUE)
                .temperature(temperature)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        try {
            ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
            return invokeModelApiResp.getFlowable();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }
}
