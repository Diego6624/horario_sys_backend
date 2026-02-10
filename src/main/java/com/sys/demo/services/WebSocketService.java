package com.sys.demo.services;

import com.sys.demo.dto.HorarioEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void enviarActualizacionHorario(String tipo, Long horarioId) {

        HorarioEvent event = new HorarioEvent(tipo, horarioId);
        
        messagingTemplate.convertAndSend(
                "/topic/horarios",
                event
        );
    }
}