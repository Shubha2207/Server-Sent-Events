package com.example.demoRealTimeNews.resources;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class NewsController {

    public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();



    //public Map<String, SseEmitter> emitters = new HashMap<>();


    //method for subscription
    @CrossOrigin
    @RequestMapping(value = "/subscribe")
    public SseEmitter subscribe()  {

        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        sendInitEvent(sseEmitter);

        emitters.add(sseEmitter);
        //emitters.put(UserID,sseEmitter);


        sseEmitter.onCompletion(()->emitters.remove(sseEmitter));
        sseEmitter.onTimeout(()->emitters.remove(sseEmitter));
        sseEmitter.onError((e)->emitters.remove(sseEmitter));

        return sseEmitter;
    }

    private void sendInitEvent(SseEmitter sseEmitter){
        try {
            sseEmitter.send(SseEmitter.event().name("INIT"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }




    //method for dispatching events to all clients
    //to specific user
    @PostMapping(value = "/dispatchEvent")
    public void dispatchEventToAllClients(@RequestParam String start) throws JSONException, InterruptedException {


        AtomicBoolean complete = new AtomicBoolean(true);
        Random rand = new Random();

        while(complete.get()){


            int mumbaiTemp=20 + rand.nextInt(25)%8;
            int puneTemp=20 + rand.nextInt(25) % 8 ;
            int delhiTemp=20 + rand.nextInt(25)% 8;
            int bangaloreTemp=20 + rand.nextInt(25)% 8;


            String eventFormatted = new JSONObject()
                    .put("mumbaiTemp",mumbaiTemp)
                    .put("puneTemp",puneTemp)
                    .put("delhiTemp",delhiTemp)
                    .put("bangaloreTemp",bangaloreTemp).toString();


            for( SseEmitter emitter : emitters){
                try{
                    emitter.send(SseEmitter.event().name("latestNews").data(eventFormatted));
                }catch (IOException e){
                    emitter.onCompletion(()-> complete.set(false));
                    emitters.remove(emitter);
                    //e.printStackTrace();
                }
            }

            Thread.sleep(5000);

        }




    }

}