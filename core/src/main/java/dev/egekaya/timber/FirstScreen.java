package dev.egekaya.timber;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class FirstScreen implements Screen {

    private ShapeRenderer shapeRenderer;
    private List<Tree> trees;
    private OrthographicCamera camera;
    private int woodCount = 0;
    private BitmapFont font;
    private SpriteBatch batch;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);
        batch = new SpriteBatch();

        trees = new ArrayList<>();
        spawnInitialTrees();
    }

    private void spawnInitialTrees() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            float x = random.nextFloat() * 1920;
            float y = random.nextFloat() * 1080;
            trees.add(new Tree(new Position(x, y)));
        }
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        handleInput();
        draw();
    }

    private void draw() {
        // camera
        ScreenUtils.clear(Color.BLACK);
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        //trees
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);

        for (Tree tree : trees) {
            shapeRenderer.circle(tree.getTreePos().getX(), tree.getTreePos().getY(), 35);
        }

        shapeRenderer.end();

        //text
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "Wood: " + woodCount, 10, 1060);
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            boolean removedTree = trees.removeIf(tree -> {
                float dx = tree.getTreePos().getX() - touchPos.x;
                float dy = tree.getTreePos().getY() - touchPos.y;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);
                return distance < 35;
            });
            if (removedTree) {
                woodCount++;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if (width <= 0 || height <= 0) {
            return;
        }

        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        shapeRenderer.dispose();
        font.dispose();
        batch.dispose();
    }
}
