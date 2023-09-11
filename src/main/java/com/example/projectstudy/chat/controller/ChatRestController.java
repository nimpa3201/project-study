package com.example.projectstudy.chat.controller;

import com.example.projectstudy.chat.service.ChatService;
import com.example.projectstudy.chat.dto.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("chat")
@RequiredArgsConstructor
public class ChatRestController {
    private final ChatService chatService;

    @GetMapping("rooms")
    public ResponseEntity<List<ChatRoom>> getChatRooms(){
        return ResponseEntity.ok(chatService.getChatRooms());
    }

    @PostMapping("rooms")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoom chatRoom){
        return ResponseEntity.ok(chatService.createChatRoom(chatRoom));
    }

    @GetMapping("rooms/{id}/name")
    public ResponseEntity<ChatRoom> getRoomName(@PathVariable("id") Long roomId) {
        return ResponseEntity.ok(chatService.findRoomById(roomId));
    }

    // 관리자만 삭제 가능
    @DeleteMapping("rooms/{id}/name")
    public ResponseEntity<String> deleteRoom(@PathVariable("id") Long roomId){

        return chatService.deleteRoom(roomId);
    }
}
