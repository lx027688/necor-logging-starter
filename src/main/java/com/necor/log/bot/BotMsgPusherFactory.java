package com.necor.log.bot;

import com.necor.bot.pusher.BotPusher;
import com.necor.bot.pusher.BotPusherFactory;
import com.necor.bot.pusher.constant.Pusher;
import com.necor.log.config.LogbackProperties;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class BotMsgPusherFactory {

    public static List<BotPusher> getBotMsgPusher(LogbackProperties.AppenderProperties.BotProperties bot){
        List<BotPusher> botMsgPushers = new ArrayList<>();

        if (ObjectUtils.isEmpty(bot)) {
            return botMsgPushers;
        }
        if (!ObjectUtils.isEmpty(bot.getLark())) {
            botMsgPushers.add(BotPusherFactory.getPusher(Pusher.LARK, bot.getLark()));
        }
        if (!ObjectUtils.isEmpty(bot.getWeixin())) {
            botMsgPushers.add(BotPusherFactory.getPusher(Pusher.WEIXIN, bot.getLark()));
        }

        return botMsgPushers;
    }

}
