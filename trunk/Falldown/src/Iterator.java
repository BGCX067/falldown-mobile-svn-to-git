
public class Iterator {

	int index;
	LinkedList list;
	
	public Iterator(LinkedList l)
	{
		list = l;
		index = 0;
	}
	
	
	public Object next()
	{
		Object toReturn = list.elementAt(index);
		index = index + 1;
		
		return toReturn;
	}
	
	public boolean hasNext()
	{
		return index < list.size();
	}
	
	
}
