package com.paradise_seeker.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.CollisionSystem;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.monster.boss.*;
import com.paradise_seeker.game.entity.monster.creep.*;
import com.paradise_seeker.game.entity.monster.elite.*;
import com.paradise_seeker.game.entity.object.*;
import com.paradise_seeker.game.entity.Monster;
import com.paradise_seeker.game.entity.npc.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMap {
	protected static final int MAP_WIDTH = 100;
    protected static final int MAP_HEIGHT = 100;

    protected Texture backgroundTexture;
    protected List<Collidable> collidables;
    protected List<GameObject> gameObjects;
    protected List<Rectangle> occupiedAreas;
    protected List<NPC1> npcs = new ArrayList<>();

    protected List<Monster> monsters;
    protected List<HPitem> hpItems = new ArrayList<>();
    protected List<MPitem> mpItems = new ArrayList<>();
    protected List<ATKitem> atkItems = new ArrayList<>();
    protected List<Skill1item> skill1Items = new ArrayList<>();
    protected List<Skill2item> skill2Items = new ArrayList<>();
    
    public Portal portal;


    protected float itemSpawnTimer = 0f;
    protected static final float ITEM_SPAWN_INTERVAL = 120f;

    public GameMap(Player player) {
        backgroundTexture = new Texture("images/map/test.png");
        gameObjects = new ArrayList<>();
        occupiedAreas = new ArrayList<>();
        monsters = new ArrayList<>();
        collidables = new ArrayList<>();

        player.bounds.x = MAP_WIDTH / 2f;
        player.bounds.y = MAP_HEIGHT / 2f;
        occupiedAreas.add(new Rectangle(player.bounds));

        generateObjects();
        generateMonsters(player);
        generateRandomItems(5, 5);
        portal = new Portal(10, 10); // Tạo cổng ở vị trí (10,10)
        collidables.add(portal);

    }

    private void generateObjects() {
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            Rectangle bounds = generateNonOverlappingBounds(2, 2);
            if (bounds != null) {
                GameObject obj;
                switch (i % 5) {
                    case 0: obj = new Tree(bounds.x, bounds.y); break;
                    case 1: obj = new Forest(bounds.x, bounds.y); break;
                    case 2: obj = new WaterLake(bounds.x, bounds.y); break;
                    case 3: obj = new LavaLake(bounds.x, bounds.y); break;
                    default: obj = new RockMountain(bounds.x, bounds.y); break;
                }
                gameObjects.add(obj);
                occupiedAreas.add(obj.getBounds());
                collidables.add(obj);
            }
        }
    }

    private void generateMonsters(Player player) {
        int bossCount = 0;
        int normalMonsterCount = 0;
        generateNPCs();

        for (int i = 0; i < bossCount; i++) {
            Rectangle b = generateNonOverlappingBounds(4, 4);
            if (b != null) spawnMonster(new Boss1(b.x, b.y), player);
        }

        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new CyanBat(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new DevilCreep(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new EvilPlant(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new YellowBat(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new RatCreep(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new FlyingCreep(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new FlyingDemon(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(4, 4);
            if (b != null) spawnMonster(new FirewormElite(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(4, 4);
            if (b != null) spawnMonster(new IceElite(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(4, 4);
            if (b != null) spawnMonster(new MinotaurElite(b.x, b.y), player);
        }
    }
    private void generateNPCs() {
        // Tạo 1 NPC1 tại vị trí ngẫu nhiên
        Rectangle bounds = generateNonOverlappingBounds(1, 1);
        if (bounds != null) {
            NPC1 npc = new NPC1(bounds.x, bounds.y);
            npcs.add(npc);
        }

        // Có thể thêm nhiều NPC hơn nếu muốn
        // Ví dụ: thêm 3 NPC
        for (int i = 0; i < 50; i++) {
            Rectangle moreBounds = generateNonOverlappingBounds(1, 1);
            if (moreBounds != null) {
                NPC1 npc = new NPC1(moreBounds.x, moreBounds.y);
                npcs.add(npc);
            }
        }
    }
    public List<NPC1> getNPCs() {
        return npcs;
    }


    private void spawnMonster(Monster monster, Player player) {
        monster.player = player;
        monsters.add(monster);
        collidables.add(monster);
        occupiedAreas.add(monster.getBounds());
    }

    private Rectangle generateNonOverlappingBounds(float width, float height) {
        Random rand = new Random();
        for (int attempts = 0; attempts < 1000; attempts++) {
            float x = rand.nextInt(MAP_WIDTH - (int) width);
            float y = rand.nextInt(MAP_HEIGHT - (int) height);
            Rectangle newBounds = new Rectangle(x, y, width, height);
            boolean overlaps = false;
            for (Rectangle occ : occupiedAreas) {
                if (occ.overlaps(newBounds)) {
                    overlaps = true;
                    break;
                }
            }
            if (!overlaps) {
                return newBounds;
            }
        }
        return null;
    }

    public void render(SpriteBatch batch) {
        batch.draw(backgroundTexture, 0, 0, MAP_WIDTH, MAP_HEIGHT);

        for (GameObject obj : gameObjects) obj.render(batch);
        for (HPitem item : hpItems) item.render(batch);
        for (MPitem item : mpItems) item.render(batch);
        for (ATKitem item : atkItems) item.render(batch);
        for (Skill1item item : skill1Items) item.render(batch);
        for (Skill2item item : skill2Items) item.render(batch);

        for (Monster m : monsters) m.render(batch);
        for (NPC1 npc : npcs) npc.render(batch);

        portal.render(batch); 
    }

    public void update(float deltaTime) {
        for (Monster m : monsters) m.update(deltaTime);
        for (NPC1 npc : npcs) npc.update(deltaTime);

        hpItems.removeIf(item -> !item.isActive());
        mpItems.removeIf(item -> !item.isActive());
        atkItems.removeIf(item -> !item.isActive());
        skill1Items.removeIf(item -> !item.isActive());
        skill2Items.removeIf(item -> !item.isActive());

        itemSpawnTimer += deltaTime;
        if (itemSpawnTimer >= ITEM_SPAWN_INTERVAL) {
            spawnRandomItem();
            itemSpawnTimer = 0f;
        }
    }

    public void checkCollisions(Player player) {
        CollisionSystem.checkCollisions(player, collidables);

        for (HPitem item : hpItems) if (item.isActive() && item.getBounds().overlaps(player.getBounds())) item.onCollision(player);
        for (MPitem item : mpItems) if (item.isActive() && item.getBounds().overlaps(player.getBounds())) item.onCollision(player);
        for (ATKitem item : atkItems) if (item.isActive() && item.getBounds().overlaps(player.getBounds())) item.onCollision(player);
        for (Skill1item item : skill1Items) if (item.isActive() && item.getBounds().overlaps(player.getBounds())) item.onCollision(player);
        for (Skill2item item : skill2Items) if (item.isActive() && item.getBounds().overlaps(player.getBounds())) item.onCollision(player);
        if (player.getBounds().overlaps(portal.innerBounds)) {
            portal.onCollision(player);
        }



    }

    public void dispose() {
        backgroundTexture.dispose();
        for (GameObject obj : gameObjects) obj.dispose();
    }

    private void generateRandomItems(int hpCount, int mpCount) {
        Random rand = new Random();

        String[] hpTextures = {"items/potion/potion3.png", "items/potion/potion4.png", "items/potion/potion5.png"};
        int[] hpValues = {20, 40, 60};

        String[] mpTextures = {"items/potion/potion9.png", "items/potion/potion10.png", "items/potion/potion11.png"};
        int[] mpValues = {15, 30, 50};

        String[] atkTextures = {"items/atkbuff_potion/potion14.png", "items/atkbuff_potion/potion15.png", "items/atkbuff_potion/potion16.png"};
        int[] atkValues = {5, 10, 15};

        for (int i = 0; i < hpCount; i++) {
            int idx = rand.nextInt(hpTextures.length);
            hpItems.add(new HPitem(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, hpTextures[idx], hpValues[idx]));
        }
        for (int i = 0; i < mpCount; i++) {
            int idx = rand.nextInt(mpTextures.length);
            mpItems.add(new MPitem(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, mpTextures[idx], mpValues[idx]));
        }
        for (int i = 0; i < 3; i++) {
            int idx = rand.nextInt(atkTextures.length);
            atkItems.add(new ATKitem(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, atkTextures[idx], atkValues[idx]));
            skill1Items.add(new Skill1item(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, "items/buff/potion12.png"));
            skill2Items.add(new Skill2item(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, "items/buff/potion13.png"));
        }
    }

    private void spawnRandomItem() {
        Random rand = new Random();
        int type = rand.nextInt(5); // 0 = HP, 1 = MP, 2 = ATK, 3 = Skill1, 4 = Skill2

        if (type == 0) {
            String[] textures = {"items/potion/potion3.png", "items/potion/potion4.png", "items/potion/potion5.png"};
            int[] values = {20, 40, 60};
            int idx = rand.nextInt(textures.length);
            hpItems.add(new HPitem(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, textures[idx], values[idx]));
        } else if (type == 1) {
            String[] textures = {"items/potion/potion9.png", "items/potion/potion10.png", "items/potion/potion11.png"};
            int[] values = {15, 30, 50};
            int idx = rand.nextInt(textures.length);
            mpItems.add(new MPitem(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, textures[idx], values[idx]));
        } else if (type == 2) {
            String[] textures = {"items/atkbuff_potion/potion14.png", "items/atkbuff_potion/potion15.png", "items/atkbuff_potion/potion16.png"};
            int[] values = {5, 10, 15};
            int idx = rand.nextInt(textures.length);
            atkItems.add(new ATKitem(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, textures[idx], values[idx]));
        } else if (type == 3) {
            skill1Items.add(new Skill1item(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, "items/buff/potion12.png"));
        } else {
            skill2Items.add(new Skill2item(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, "items/buff/potion13.png"));
        }
    }

    public void damageMonstersInRange(float x, float y, float radius, int damage) {
        for (Monster m : monsters) {
            if (!m.isDead() && isInRange(x, y, m.getBounds(), radius)) m.takeDamage(damage);
        }
    }

    private boolean isInRange(float x, float y, Rectangle bounds, float radius) {
        float centerX = bounds.x + bounds.width / 2;
        float centerY = bounds.y + bounds.height / 2;
        float dx = centerX - x;
        float dy = centerY - y;
        return dx * dx + dy * dy <= radius * radius;
    }
    



    public List<Monster> getMonsters() { return monsters; }
}
