package softuni.bg.finalPJ.service;

import softuni.bg.finalPJ.models.entities.Message;

import java.util.List;

public interface MessageService {

    void saveMessage(Message message, Long userId);

    List<Message> getMessagesForUser(Long userId);

    void deleteMessage(Long messageId, Long userId);
}
