package logic;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

public class BotMain extends TelegramLongPollingBot {

    ticTacToe ticTacToe1 = new ticTacToe();
    public static List<Long> playersID = new ArrayList<>();
    public static int gameFlag = 0;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                if (playersID.contains(message.getChatId()) && gameFlag == 1) {
                    if ("/exit".equals(message.getText())) {
                        sendMsg(message, "You are out of the game");
                        playersID.remove(message.getChatId());
                        gameFlag = 0;
                    } else {
                        sendMsg(message, ticTacToe1.game(message.getText(), message.getChatId()));
                    }
                } else
                    switch (message.getText()) {
                        case "/game" -> {
                            sendMsg(message, "Let's begin the game!" +
                                    "\nChoose your position from 1 to 9:");
                            sendMsg(message, ticTacToe1.userCheck(message.getChatId()));
                            playersID.add(message.getChatId());
                            gameFlag = 1;
                        }
                        case "/help" -> sendMsg(message, "/game - to begin Tic Tac Toe game.\n" +
                                "/joke - the bot will send you a joke.");
                        case "/joke" -> sendMsg(message, "pudge povesilsya(");
                        default -> sendMsg(message, "I can't understand you, use the /help command!");
                    }
            }
        }
    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getBotUsername() {
        return "@UD_Haevy_Test_bot";
    }

    @Override
    public String getBotToken() {
        return "5518915398:AAHh5Gq2RRRjpEVaHTajEHmLQBcq0TL-IRs";
    }

    public static void main(String[] args) throws TelegramApiException {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(new BotMain());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
