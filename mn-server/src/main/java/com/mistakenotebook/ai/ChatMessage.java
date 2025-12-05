package com.mistakenotebook.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 聊天消息模型
 * 
 * @author simon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    /**
     * 角色: user 或 model
     */
    private String role;

    /**
     * 消息内容
     */
    private String content;

    public static ChatMessage user(String content) {
        return new ChatMessage("user", content);
    }

    public static ChatMessage model(String content) {
        return new ChatMessage("model", content);
    }
}
