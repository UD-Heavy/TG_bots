package bot;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.toIntExact;

public class Logic extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "UD_Heavy_bot";
    }

    @Override
    public String getBotToken() {
        return "5367855719:AAFQDpSMBPFRAi4zqeM2vi4jwIAHpjueAvs";
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.toString());
        Message message = update.getMessage();
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatID = message.getChatId();
            String messageText = update.getMessage().getText();
            System.out.println("-----------------------");
            String userName = getUserName(update);
            System.out.println(userName);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));
            if (messageText.equals("/start")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatID);
                sendMessage.setText("You send /start");
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton()
                        .builder()
                        .text("Message update")
                        .callbackData("Message_update")
                        .build());
                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);
                sendMessage.setReplyMarkup(markupInline);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (messageText.equals("/pic")) {
                long chatId = message.getChatId();
                String answer = EmojiParser.parseToUnicode("Here is a smile emoji: :smile:\n\n Here is alien emoji: :alien:");
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText(answer + "\n i can't send photo! :(");
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (update.getMessage().getText().equals("/markup")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatID);
                sendMessage.setText("Here is your keyboard");
                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                List<KeyboardRow> keyboard = new ArrayList<>();
                KeyboardRow row = new KeyboardRow();
                row.add("row 1 - button 1");
                row.add("row 1 - button 2");
                row.add("row 1 - button 3");
                keyboard.add(row);
                row = new KeyboardRow();
                row.add("row 1 - button 1");
                row.add("row 2 - button 2");
                row.add("row 3 - button 3");
                keyboard.add(row);
                keyboardMarkup.setOneTimeKeyboard(true);
                keyboardMarkup.setKeyboard(keyboard);
                sendMessage.setReplyMarkup(keyboardMarkup);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (messageText.equals("row 1 - button 1")) {
                try {
                    execute(SendMessage.builder().text("ne umeyu").chatId(chatID).build());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (messageText.equals("/hide")) {
                SendMessage msg = new SendMessage();
                msg.setText("Keyboard hidden");
                msg.setChatId(chatID);
                ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
                replyKeyboardRemove.setRemoveKeyboard(true);
                msg.setReplyMarkup(replyKeyboardRemove);
                try {
                    execute(msg);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    execute(SendMessage.builder()
                            .chatId(update.getMessage().getChatId())
                            .text("Unknown command!")
                            .build());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("Message_update")) {
                String answer = "Updated message text";
                EditMessageText new_message = new EditMessageText();
                new_message.setChatId(chat_id);
                new_message.setMessageId(toIntExact(message_id));
                new_message.setText(answer);
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getUserName(Update update) {
        User user = update.getMessage().getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getFirstName(), user.getLastName());
    }
}

//handleMessage(update.getMessage())
    /*private void handleMessage(Message message) throws TelegramApiException {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/set_currency":
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        execute(SendMessage.builder()
                                .text("Please choose Original and Target currencies")
                                .chatId(message.getChatId().toString())
                                .replyMarkup(InlineKeyboardMarkup.builder().build())
                                .build());
                        return;
                }
            }
        }
    }*/

/*long chatId = update.getMessage().getChatId();
            List<PhotoSize> photos = update.getMessage().getPhoto();
            SendMessage sendMessage = new SendMessage();
            String f_id = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getFileId();
            int f_width = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getWidth();
            int f_heigth = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getHeight();
            String caption = "file id: " + f_id +
                    "\nwigth: " + Integer.toString(f_width) +
                    "\nheigth: " + Integer.toString(f_heigth);
            System.out.println("vce norm");
            SendPhoto msg = new SendPhoto();
            msg.setChatId(chatId);
            msg.setCaption(caption);
            sendMessage.setText(f_id);
            sendMessage.setChatId(chatId);

            System.out.println("poka vce norm");
            try {
                execute(sendMessage);
            } catch (Exception e) {
                e.printStackTrace();*/