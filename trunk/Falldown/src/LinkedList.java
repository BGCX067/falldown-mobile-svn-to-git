
/**
 * Implementation d'une lighteight linkedlist sous forme de tableau pour
 * J2ME
 * 
 */
public class LinkedList 
{
	Object[] array;
	int begin, end;
	
	public LinkedList()
	{
		this(14);
	}
	

	
	public LinkedList(int arraySize)
	{
		array = new Object[arraySize];
		begin = 0;
		end = 0;
	}
	
	public LinkedList getCopy()
	{
		LinkedList toReturn = new LinkedList(array.length);
		System.arraycopy(array, 0, toReturn.array, 0, array.length);
		
		toReturn.begin = begin;
		toReturn.end = end;
		
		return toReturn;
	}
	
	public void addLast(Object o)
	{
		if(size() == array.length-1)
		{
			Object[] newArray = new Object[array.length*2];
			int i=0;
			Iterator it = this.iterator();
			while(it.hasNext())
			{
				newArray[i] = it.next();
				i++;
			}
			newArray[i]=o;
			begin = 0;
			end = i+1;
		}
		
		array[end]=o;
		end = (end+1) % array.length;
	}
	
	public Object removeFirst()
	{
		if(size() == 0)
		{
			//TODO
		}
		
		Object toReturn =  array[begin];
		array[begin] = null;
		begin = (begin +1) % array.length;
		return toReturn;
	}
	
	public Object getFirst()
	{
		if(size()==0)
		{
			// TODO
		}
		
		return array[begin];
	}
	
	public int size()
	{
		if (end>=begin)
		{
			return end-begin;
		}
		else
		{
			return array.length - begin + end;
		}
	}
	
	public boolean isEmpty()
	{
		return size()==0;
	}
	
	public Object elementAt(int index)
	{
		if (index >= size())
		{
			// TODO
		}
		if(array[(begin + index)%array.length]==null)
		{
			System.out.println(begin + " " + end + " " + index);
		}
			
		return array[(begin + index)%array.length];
	}
	
	
	public Iterator iterator()
	{
		return new Iterator(this);
	}
	

}
