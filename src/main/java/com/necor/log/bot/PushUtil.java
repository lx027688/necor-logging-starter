package com.necor.log.bot;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.necor.bot.pusher.BotPusher;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PushUtil {

    static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void multiplePush(List<BotPusher> botMsgPushers, ILoggingEvent event) {
        try {
            botMsgPushers.forEach(pusher -> {
                executorService.execute(()->{
                    try{
                        pusher.push(event.getLevel().levelStr, event.getFormattedMessage());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
