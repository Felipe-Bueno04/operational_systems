import java.util.ArrayList;
import java.util.List;

public class Room {
    private int id;

    private String name;

    private String soundPath;

    private List<Room> rooms = new ArrayList<>();

    public Room(int id, String name, String soundPath) {
        this.id = id;
        this.name = name;
        this.soundPath = soundPath;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Room> getRooms() {
        return this.rooms;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public Room searchRoomById(int id) {
        for (Room room : this.rooms) {
            if (room.id == id) return room;
        }

        return null;
    }

    public String getSoundPath() {
        return this.soundPath;
    }
}
