package com.sys.demo.services;

import com.sys.demo.dto.ScheduleViewDTO;
import java.util.List;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Enviar lista completa de horarios con estado actual
    public void enviarEstadoActual(List<ScheduleViewDTO> data) {
        messagingTemplate.convertAndSend("/topic/horarios", data);
    }
}
