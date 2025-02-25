public class Dungeon {
    private Room currentRoom;

    public Dungeon() {
        Room starterRoom = new Room(0, "Main", "mainroom.wav");
        Room merchantRoom = new Room(1, "Merchant", "merchantroom.wav");
        merchantRoom.addRoom(starterRoom);

        Room bossRoom = new Room(2, "Boss", "bossroom.wav");
        bossRoom.addRoom(starterRoom);

        Room saveRoom = new Room(3, "Save Room", "saveroom.wav");
        saveRoom.addRoom(starterRoom);

        this.currentRoom = starterRoom;

        currentRoom.addRoom(merchantRoom);
        currentRoom.addRoom(bossRoom);
        currentRoom.addRoom(saveRoom);
    }

    public void gotoRoom(Room room) {
        this.currentRoom = room;
    }

    public Room getCurrentRoom() {
        return this.currentRoom;
    }
}
