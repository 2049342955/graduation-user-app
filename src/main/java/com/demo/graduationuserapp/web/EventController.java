package com.demo.graduationuserapp.web;

import com.demo.graduationuserapp.config.event.RequestMessage;
import com.demo.graduationuserapp.config.event.ResponseMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class EventController {

    @MessageMapping("/welcome")
    @SendTo("/topic/getResponse")
    public ResponseMessage say(RequestMessage message) {
        System.out.println(message.getName());
        return new ResponseMessage("您的活动已经被审核，请查收!");
    }
}
