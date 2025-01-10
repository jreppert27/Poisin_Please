package tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import main.GamePanel;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public static final int GRASS = 0;
    public static final int WALL = 1;
    public static final int WATER = 2;
    public static final int TREE = 3;
    public static final int SAND = 4;
    public static final int EARTH = 5;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/res/maps/world_map.txt");
    }

    public void ensureTilesLoaded() {
        if (tile[0] == null) {
            getTileImage();
        }
    }

    public void getTileImage() {
        try {
            tile[GRASS] = new Tile();
            tile[GRASS].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/grass_tile.png"));

            tile[WALL] = new Tile();
            tile[WALL].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/wall_tile.png"));
            tile[WALL].collision = true;

            tile[WATER] = new Tile();
            tile[WATER].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/water_tile.png"));
            tile[WATER].collision = true;

            tile[TREE] = new Tile();
            tile[TREE].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/tree_tile.png"));
            tile[TREE].collision = true;

            tile[SAND] = new Tile();
            tile[SAND].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/sand_tile.png"));

            tile[EARTH] = new Tile();
            tile[EARTH].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/earth_tile.png"));

        } catch (IOException e) {
            System.err.println("Error loading tile image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();

                while (col < gp.maxWorldCol) {
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();

        } catch (Exception e) {

        }
    }

    public void draw(Graphics2D g2) {
        ensureTilesLoaded();

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenX) {

                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

    }
}
