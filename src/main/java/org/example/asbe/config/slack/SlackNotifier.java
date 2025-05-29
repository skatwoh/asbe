package org.example.asbe.config.slack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SlackNotifier {

    @Value("${app.slack.webhookUrl}")
    private String webhookUrl = "";

    public void sendError(String title, String content) {
        Map<String, Object> message = new HashMap<>();
        message.put("text", "*[ERROR]* " + title + "\n```" + content + "```");
        new RestTemplate().postForObject(webhookUrl, message, String.class);
    }
}
