package cave2.structures;


import java.util.*;
import java.util.concurrent.*;

/**
 * A set that can be locked, so that the set remains unchanged for iteration.
 * During the time that the set is locked, add/remove tasks are added to a buffer,
 * so that when the set is unlocked, the buffer's tasks are applied. 
 *
 * This implementation should be more efficient than java.util.concurrent.CopyOnWriteArray,
 * which provides similar functionality.
 *
 * The iterator of this class does not support the remove() operation.
 */
public class BufferedSet<T> extends AbstractCollection<T> implements Iterable<T>
{
	private static enum ActionID
	{
		ADD,
		REMOVE
	}
	private static class Action
	{
		ActionID type;
		Object data;

		public Action()
		{
			this(null,null);
		}
		public Action(ActionID type)
		{
			this(type,null);
		}
		public Action(ActionID type, Object data)
		{
			this.type = type;
			this.data = data;
		}

		public String toString()
		{
			if(type == ActionID.ADD)
				return "+"+data.toString();
			else if(type == ActionID.REMOVE)
				return "-"+data.toString();
			assert(false);
			return null;
		}
	}
	
	private class BSIter implements Iterator<T>
	{
		public Iterator<Map.Entry<T,Boolean>> iter;
		public BSIter()
		{
			iter = main.entrySet().iterator();
		}

		public boolean hasNext() {
			return iter.hasNext();
		}

		public T next()
		{
			return iter.next().getKey();
		}

		public void remove() {
			throw new UnsupportedOperationException("remove() not supported.");
		}
	}

	private static final int ACTIONCACHE_SIZE = 20;
	private static final List<Action> actionCache = new LinkedList<Action>();
	static
	{
		for(int i=0; i<ACTIONCACHE_SIZE; i++)
			actionCache.add(new Action());
	}

	private static Action getAction(ActionID id, Object data)
	{
		Action a;
		if(actionCache.isEmpty())
			a = new Action();
		else
			a = actionCache.remove(0);

		a.type = id;
		a.data = data;
		return a;
	}
	private static void returnAction(Action a)
	{
		a.data = null;
		actionCache.add(a);
	}

	private ConcurrentHashMap<T,Boolean> main;
	private ConcurrentLinkedQueue<Action> buffer;
	private boolean locked;
	
	public BufferedSet()
	{
		main = new ConcurrentHashMap<T,Boolean>();
		buffer = new ConcurrentLinkedQueue<Action>();
	}
	
	/**
	 * Attempts to lock the list.
	 * @throws IllegalStateException If the set is already locked.
	 */
	public synchronized void lock()
	{
		if(locked) throw new IllegalStateException("lock() called on already locked set.");
		locked = true;
	}
	/**
	 * Unlocks the set and applies the queued tasks.
	 * @throws IllegalStateException If the set is not locked.
	 */
	@SuppressWarnings("unchecked")
	public synchronized void unlock()
	{
		if(!locked) throw new IllegalStateException("unlock() called on unlocked set.");
		while(!buffer.isEmpty())
		{
			Action a = buffer.poll();
			if(a.type == ActionID.ADD)
				main.put((T)(a.data),true); // TODO: Causes unchecked cast warning...
			else if(a.type == ActionID.REMOVE)
				main.remove((T)(a.data));
			returnAction(a);
		}
		locked = false;
	}
	
	/**
	 * Adds an item to the set, or the buffer if the set is locked.
	 */
	public boolean add(T o)
	{
		if(locked)
			buffer.add(getAction(ActionID.ADD, o));
		else
			main.put(o,true);
		return true;
	}
	
	/**
	 * Removes an item from the set, or queues the item for removal if
	 * the set is locked.
	 * TODO: Fix "Unchecked/Unsafe operation" error
	 */
	public boolean remove(Object o)
	{
		if(locked)
			buffer.add(getAction(ActionID.REMOVE,o));
		else
			main.remove(o); // TODO: Causes suspicious call warning...
		return true;
	}
	
	/**
	 * Returns true if the set contains object o. This method does not
	 * look at the task queue if the set is locked, so if you lock the
	 * set, remove an item, and then call this with the removed item,
	 * it will return true.
	 *
	 * TODO: Fix "Unchecked/Unsafe operation" error
	 */
	public boolean contains(Object o)
	{
		return main.containsKey(o); // TODO: Causes suspicious call warning...
	}

	/**
	 * Returns the size of the list. Does not apply the tasks.
	 */
	public int size()
	{
		return main.size();
	}

	/**
	 * The iterator. remove() is not supported. Does not throw ConcurrentModificationException,
	 * even if the set is not locked.
	 * (Technically uses iterator from java.util.concurrent.ConcurrentHashMap)
	 */
	public Iterator<T> iterator()
	{
		return new BSIter();
	}

	public String toString()
	{
		String str = main.toString() + ", {";
		for(Action t : buffer)
		{
			str += t.toString() + ",";
		}
		return str + "}";

	}

	/**
	 * Test program.
	 */
	public static void main(String[] argz)
	{
		BufferedSet<Integer> set = new BufferedSet<Integer>();
		set.add(0);
		set.add(2);
		set.add(5);
		System.out.println(set);
		set.lock();
		set.add(6);
		set.add(7);
		set.remove(2);
		System.out.println(set);
		set.unlock();
		System.out.println(set);
	}
}
