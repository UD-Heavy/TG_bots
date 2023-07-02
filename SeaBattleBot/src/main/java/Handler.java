import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

import static java.lang.Math.toIntExact;

public class Handler extends TelegramLongPollingBot {

    Queue<Long> playerList = new LinkedList<>();
    HashMap<Long, Long> playersInGame = new HashMap<>();
    List<MyUser> rankedList = new ArrayList<>();
    Game game = new Game();
    String TEXT_MENU_1 = EmojiParser.parseToUnicode(":joystick: Play");
    String TEXT_MENU_2 = EmojiParser.parseToUnicode(":trophy: Top 10 players");
    String BEGIN_GAME_MENU_1 = EmojiParser.parseToUnicode(":joystick: Start game");
    String BEGIN_GAME_MENU_2 = EmojiParser.parseToUnicode(":back: Back");

    @Override
    public String getBotUsername() {
        return "MyFirstTest1000Bot";
    }

    @Override
    public String getBotToken() {
        return "5416799705:AAG6jJB6t4S2YjETV6Hddq_5KBAZjNSRQu8";
    }

    @Override
    public void onUpdateReceived(Update update) {
        //System.out.println(update.toString());
        Message message = update.getMessage();
        if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callData.equals("BeginTheGame")) {                              //меню игры
                String answer = EmojiParser.parseToUnicode(":earth_africa: Map:\n");
                EditMessageText newMessage = new EditMessageText();
                newMessage.setChatId(chatId);
                newMessage.setMessageId(toIntExact(messageId));
                newMessage.setText(answer);
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton()
                        .builder()
                        .callbackData("startGame")
                        .text(BEGIN_GAME_MENU_1)
                        .build());
                rowsLine.add(rowInline);
                rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton()
                        .builder()
                        .callbackData("goToMainMenu")
                        .text(BEGIN_GAME_MENU_2)
                        .build());
                rowsLine.add(rowInline);
                markupInline.setKeyboard(rowsLine);
                newMessage.setReplyMarkup(markupInline);
                try {
                    execute(newMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (callData.equals("ShowRankedList")) {                        //лист рекордов
                String answer = EmojiParser.parseToUnicode(":trophy: Top 10:\n");
                EditMessageText newMessage = new EditMessageText();
                newMessage.setChatId(chatId);
                newMessage.setMessageId(toIntExact(messageId));
                newMessage.setText(answer);
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton()
                        .builder()
                        .callbackData("goToMainMenu")
                        .text(BEGIN_GAME_MENU_2)
                        .build());
                rowsLine.add(rowInline);
                markupInline.setKeyboard(rowsLine);
                newMessage.setReplyMarkup(markupInline);
                try {
                    execute(newMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (callData.equals("startGame")) {                          //начать поиск
                CheckGamer(update);
                SendMessage sendMessage = new SendMessage();
                playerList.offer(914885741L); // потом убрать
                if (playerList.size() >= 2) {
                    Long firstID = playerList.poll();
                    Long secondID = playerList.poll();
                    Long gamePassword = firstID % secondID + firstID / secondID + firstID / 224142 + secondID % 43573;
                    sendMessage.setText(game.createGameSession(firstID, secondID));
                    sendMessage.setChatId(chatId); ///////// двум людям сделать отправку
                    playersInGame.put(firstID, gamePassword);
                    playersInGame.put(secondID, gamePassword);
                    InlineKeyboardMarkup gameKeyBoard = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> gameButtonLine = new ArrayList<>();
                    List<InlineKeyboardButton> gameButton = new ArrayList<>();
                    gameButton.add(new InlineKeyboardButton()
                            .builder()
                            .callbackData("ContinueGame")
                            .text("shoot to place A1")
                            .build());
                    gameButtonLine.add(gameButton);
                    gameButton = new ArrayList<>();
                    gameButton.add(new InlineKeyboardButton()
                            .builder()
                            .callbackData("ExitMenu")
                            .text("Exit to the game")
                            .build());
                    gameButtonLine.add(gameButton);
                    gameKeyBoard.setKeyboard(gameButtonLine);
                    sendMessage.setReplyMarkup(gameKeyBoard);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessage.setText("You are in the queue to find an opponent");
                    sendMessage.setChatId(chatId);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else if (callData.equals("goToMainMenu")) {                       //главное меню
                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                answerCallbackQuery.setShowAlert(true);
                answerCallbackQuery.setCallbackQueryId(String.valueOf(update.getCallbackQuery().getId()));
                answerCallbackQuery.setCacheTime(1);
                answerCallbackQuery.setText("You are not in line to find an opponent");
                EditMessageText editMessage = new EditMessageText();
                removeGamer(update);
                editMessage.setParseMode(ParseMode.HTML);
                String answer = EmojiParser.parseToUnicode(":bust_in_silhouette: <b>"
                        + update.getCallbackQuery().getFrom().getFirstName()
                        + "</b>, Welcome to the sea battle game!\n\n"
                        + String.format(":busts_in_silhouette: Online: %s users.", playerList.size()));
                editMessage.setChatId(chatId);
                editMessage.setMessageId(toIntExact(messageId));
                editMessage.setText(answer);
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton()
                        .builder()
                        .text(TEXT_MENU_1)
                        .callbackData("BeginTheGame")
                        .build());
                rowsLine.add(rowInline);
                rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton()
                        .builder()
                        .text(TEXT_MENU_2)
                        .callbackData("ShowRankedList")
                        .build());
                rowsLine.add(rowInline);
                markupInline.setKeyboard(rowsLine);
                editMessage.setReplyMarkup(markupInline);
                try {
                    execute(editMessage);
                    execute(answerCallbackQuery);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (callData.equals("ContinueGame")) {
                EditMessageText editMessage = new EditMessageText();
                InlineKeyboardMarkup gameKeyBoard = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> gameButtonLine = new ArrayList<>();
                List<InlineKeyboardButton> gameButton = new ArrayList<>();
                gameButton.add(new InlineKeyboardButton()
                        .builder()
                        .callbackData("ContinueGame")
                        .text("shoot to place A1")
                        .build());
                gameButtonLine.add(gameButton);
                gameButton = new ArrayList<>();
                gameButton.add(new InlineKeyboardButton()
                        .builder()
                        .callbackData("ExitMenu")
                        .text("Exit to the game")
                        .build());
                gameButtonLine.add(gameButton);
                gameKeyBoard.setKeyboard(gameButtonLine);
                editMessage.setReplyMarkup(gameKeyBoard);
                editMessage.setChatId(chatId);
                editMessage.setText(update.getCallbackQuery().getFrom().getFirstName() + "\n" + game.createGameSession(playersInGame.get(chatId), (playersInGame.get(chatId))));
                editMessage.setMessageId(toIntExact(messageId));
                try {
                    execute(editMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                } //playersInGame.containsKey(update.getCallbackQuery().getData().equals("ContinueGame"))
            }
        } else{                                                                //главное меню - ответ
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setParseMode(ParseMode.HTML);
            String answer = EmojiParser.parseToUnicode(":bust_in_silhouette: <b>"
                    + update.getMessage().getFrom().getFirstName()
                    + "</b>, Welcome to the sea battle game!\n\n"
                    + String.format(":busts_in_silhouette: Online: %s users.", playerList.size()));
            sendMessage.setText(answer);
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton()
                    .builder()
                    .text(TEXT_MENU_1)
                    .callbackData("BeginTheGame")
                    .build());
            rowsLine.add(rowInline);
            rowInline = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton()
                    .builder()
                    .text(TEXT_MENU_2)
                    .callbackData("ShowRankedList")
                    .build());
            rowsLine.add(rowInline);
            markupInline.setKeyboard(rowsLine);
            sendMessage.setReplyMarkup(markupInline);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void CheckGamer(Update update) {
        if (!playerList.contains(update.getCallbackQuery().getMessage().getChatId())) {
            playerList.add(update.getCallbackQuery().getMessage().getChatId());
        } else {
            System.out.println("This user is playing now");
        }
    }

    private void removeGamer(Update update) {
        if (playerList.contains(update.getCallbackQuery().getMessage().getChatId())) {
            playerList.remove(update.getCallbackQuery().getMessage().getChatId());
            System.out.println("The user has benn removed from the player queue");
        }

    }
}