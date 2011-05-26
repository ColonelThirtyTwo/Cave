
package cave2.entities.types;

import cave2.entities.CollisionEntity;
import cave2.entities.Creature;
import cave2.entities.Entity;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.LogU;
import cave2.structures.ResourceManager;
import cave2.structures.Vec2;
import cave2.tile.World;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.opengl.Texture;

/**
 * A zombie. Walks randomly until in range.
 * @author Colonel 32
 */
public class Zombie extends Creature
{
	private static final Logger log = LogU.getLogger();
	private static final int SPAWN_RADIUS = 40;
	private static int zombies_out = 0;
	public static class AmbientSpawner implements Entity
	{

		private World w;
		private Vec2 spawnpoint;

		public AmbientSpawner()
		{
			w = null;
			spawnpoint = new Vec2();
		}

		private void findSpawnPoint()
		{
			int c = 0;
			do
			{
				double theta = Math.random() * Math.PI * 2.0;
				double dist = Math.random() * SPAWN_RADIUS;
				spawnpoint.x = Math.cos(theta)*dist + ply.getX();
				spawnpoint.y = Math.sin(theta)*dist + ply.getY();
				c++;
			}
			while(
					w.getCamera().getCameraBounds().contains(spawnpoint.x, spawnpoint.y) ||
					w.getTileAt(spawnpoint.x, spawnpoint.y) == null ||
					w.getTileAt(spawnpoint.x, spawnpoint.y).isSolid());
		}

		public void think(int timeDelta)
		{
			assert(w != null);
			assert(ply != null);

			if(zombies_out < 30 && Math.random() > 0.99)
			{
				findSpawnPoint();
				log.log(Level.INFO, "Spawning zombie at {0}", spawnpoint);
				w.addEntity(new Zombie(spawnpoint.x, spawnpoint.y));
			}
		}

		public void draw(AABB clip)
		{
		}

		public double getX()
		{
			return 0;
		}

		public double getY()
		{
			return 0;
		}

		public void addedToWorld(World w)
		{
			this.w = w;
			zombies_out++;
		}

		public void removedFromWorld(World w)
		{
			w = null;
			zombies_out--;
		}

		public World getWorld()
		{
			return w;
		}

	}

	private static final int SIGHT_RADIUS = 5;
	private static final int SIGHT_RADIUS2 = SIGHT_RADIUS*SIGHT_RADIUS;
	
	private static Player ply;
	public static void setPlayer(Player p)
	{
		ply = p;
	}
	
	private boolean isChasing;
	private int movetimer;

	public Zombie(double x, double y)
	{
		super(x, y, 0.45, 0.45, 20);
		movetimer = 0;
		isChasing = false;
	}

	protected double getSpeed()
	{
		return 0.7;
	}

	public void collidedWith(CollisionEntity e)
	{
		if(e == ply)
		{
			Creature c = (Creature)e;
			c.damage("necrotic", 10.0);
		}
	}

	public void think(int timeDelta)
	{
		if(world == null) return;
		assert(ply != null);

		move(timeDelta);
		doTileCollisions();
		doTileCallbacks();

		double dx = ply.getX() - getX();
		double dy = ply.getY() - getY();
		double dist2 = dx*dx + dy*dy;

		if(dist2 >= SPAWN_RADIUS*SPAWN_RADIUS)
		{
			// Remove zombies straying too far from the player
			log.log(Level.INFO, "Zombie destroyed from straying too far from player.");
			world.removeEntity(this);
		}

		if(isChasing)
		{
			if(dx > -0.1 && dx < 0.1) setMovementX(0);
			else setMovementX((int)Math.signum(dx));
			if(dy > -0.1 && dy < 0.1) setMovementY(0);
			else setMovementY((int)Math.signum(dy));

			if(dist2 >= SIGHT_RADIUS2)
			{
//				movetimer -= timeDelta;
//				if(movetimer <= 0)
					isChasing = false;
			}
//			else
//				movetimer = 1000;
		}
		else
		{
			// Proceed on a random course
			movetimer -= timeDelta;
			if(movetimer <= 0)
			{
				setMovementX((int)(Math.random() * 3.0) - 1);
				setMovementY((int)(Math.random() * 3.0) - 1);
				movetimer = (int)(Math.random() * 2000.0) + 3000;
				log.log(Level.FINE, "Zombie changing direction.");
			}
			if(dist2 < SIGHT_RADIUS2)
			{
				log.log(Level.FINE,"Zombie found player, attacking");
				movetimer = 1000;
				isChasing = true;
			}
		}

	}

	public void draw(AABB clip)
	{
		if(!box.overlaps(clip)) return;
		if(world == null) return;
		Texture t = ResourceManager.getImage("entities/zombie.png");
		RenderUtil.drawImage(t, box, 1);
	}

}
