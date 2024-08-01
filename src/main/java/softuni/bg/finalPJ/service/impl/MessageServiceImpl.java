package softuni.bg.finalPJ.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import softuni.bg.finalPJ.models.entities.Message;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.MessageRepository;
import softuni.bg.finalPJ.repositories.UserRepository;
import softuni.bg.finalPJ.service.MessageService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {


    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveMessage(Message message, Long userId) {
        UserEntity receiver = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        message.setReceiver(receiver);
        message.setDate(LocalDateTime.now());
        messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesForUser(Long userId) {
        UserEntity receiver = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return messageRepository.findByReceiver(receiver);
    }

    @Override
    public void deleteMessage(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));
        if (message.getReceiver().getId().equals(userId)) {
            messageRepository.delete(message);
        } else {
            throw new AccessDeniedException("You are not authorized to delete this message");
        }
    }

}
