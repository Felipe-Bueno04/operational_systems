import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Main {
    private static Dungeon dungeon = new Dungeon();
    private static SoundPlayer roomSound;
    private static String debugErrors;
    private static String basePath = "C:\\Users\\Aluno\\projetos\\OperationalSystems\\atividade1\\audios\\";

    public static void main(String[] args) {
        boolean shouldExit = false;
        boolean debug = true;

        while(!shouldExit) {
            clearScreen();
            System.out.println("Você está em: " + dungeon.getCurrentRoom().getName());
            List<Room> rooms = dungeon.getCurrentRoom().getRooms();
            for (int i = 0; i < rooms.size(); i++) {
                Room room = dungeon.getCurrentRoom().getRooms().get(i);
                System.out.println(i + 1 + " - Entrar em " + room.getName());
            }
            System.out.println("Exit - Fechar programa");
            if (debug) {
                System.out.println(debugErrors);
            }

            String input = receiveUserInput();
            if (input.equals("Exit")) shouldExit = true;
            try {
                int index = Integer.parseInt(input);
                Room possibleRoom = rooms.get(index - 1);
                if (possibleRoom != null) changeRoom(possibleRoom);
            } catch (Exception e) {
                System.out.println("Não é um ID nem exit");
            }
        }
    }

    private static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    private static String receiveUserInput() {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object

        String input = myObj.nextLine();  // Read user input
        return input;
    }

    private static void changeRoom(Room room) {
        dungeon.gotoRoom(room);
        try {
            changeRoomTrack(room.getSoundPath());
        } catch (Exception exception) {
            debugErrors = exception.getMessage();
        }
    }

    private static void changeRoomTrack(String audioPath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new File(basePath + audioPath));
        roomSound = new SoundPlayer(ais);
        roomSound.play();
    }
}

