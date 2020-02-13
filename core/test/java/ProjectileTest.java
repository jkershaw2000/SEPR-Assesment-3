import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.sprites.Projectile;
import com.mygdx.game.sprites.Unit;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class unitForProjectile extends Unit {

    public unitForProjectile(Vector2 position, int width, int height, Texture texture, int maxHealth) {
        super(position, width, height, texture, maxHealth);
    }
}


@RunWith(Parameterized.class)
public class ProjectileTest {
    private Unit instance;
    private boolean expected;
    private Projectile projectile;

    private static Unit instance1 = new unitForProjectile(new Vector2(200, 200),100, 100, null,
            100);
    private static Unit instance2 = new unitForProjectile(new Vector2(150, 150),100, 100, null,
            100);
    private static Unit instance3 = new unitForProjectile(new Vector2(0, 0),1, 1, null,
            100);



    @Before
    public void initialise(){

        projectile = new Projectile(new Vector2(100, 100), 10, 10, null,
                new Vector2(200, 200), 10.0f, 10, 10, "Straight");

    }

    public ProjectileTest(Unit instance, boolean expected){
        this.instance = instance;
        this.expected = expected;
    }

    @Test
    public void testEightParameterConstructorWorksAsExpected() {
        Assertions.assertEquals(projectile.getMaxLength(), 10);
        Assertions.assertEquals(projectile.getDamage(), 10);
        Assertions.assertEquals(projectile.getSpeed(), 10.0f);
        Assertions.assertEquals(projectile.getDirection(), new Vector2(100, 100).nor());
    }

    @Test
    public void testSevenParameterConstructorWorksAsExpected() {
        Projectile p = new Projectile(new Vector2(100, 100), 10, 10, null,
                new Vector2(200, 200), 10.0f, 10, "Straight");
        Assertions.assertEquals(p.getDamage(), 10);
        Assertions.assertEquals(p.getSpeed(), 10.0f);
        Assertions.assertEquals(p.getDirection(), new Vector2(100, 100).nor());
    }

    @Test
    public void setLengthShouldReturnDistanceBetweenVectors() {
        projectile.update();
        projectile.setLength();
        System.out.println(projectile.getLength());
        assertEquals(10.0, projectile.getLength());
    }

    //ASSESSMENT 3
    @Test
    public void randomWaterStreamsShouldInitialiseWithinRangeTest() {
        Projectile p = new Projectile(new Vector2(100, 100), 10, 10, null,
                new Vector2(200, 200), 10.0f, 10, 10, "Random");
        assertTrue(p.getDirection().x > -100 && p.getDirection().x <= 310);
        assertTrue(p.getDirection().y > -100 && p.getDirection().y <= 310);
    }

    @Parameters
    public static Collection input() {
        return Arrays.asList(new Object[][] { {instance1, true}, {instance2, true}, {instance3, false} });
    }


    //Testing through parameterized testing that hitUnit() works with both false and true values.
    @Test
    public void testHitUnit() {
        System.out.println("The projectile aiming at Unit should: " + expected);
        assertEquals(expected, projectile.hitUnit(instance));
    }

}


