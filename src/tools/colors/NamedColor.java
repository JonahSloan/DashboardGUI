package tools.colors;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("serial")
public class NamedColor extends Color
{
	private static final ArrayList<NamedColor> colors = getColors();
	/**
	 * Gets all of the Color fields from NamedColor
	 */
	private static ArrayList<NamedColor> getColors()
	{
		Field[] fields = ColorInts.class.getDeclaredFields(); //TODO
		ArrayList<NamedColor> arr = new java.util.ArrayList<NamedColor>();
		for(Field f : fields)
		{
			try{
				arr.add(new NamedColor(f.getName(), (int)f.get(null)));
			}catch(Exception e){}
		}
		return arr;
	}
	private final String name;
	public NamedColor(String name, int rgbacolor)
	{
		super(rgbacolor, true);
		this.name=name;
	}
	public int getARGB()
	{
		return getARGB(this);
		//return (getAlpha()<<24) | (getRed()<<16) | (getGreen()<<8) | (getBlue()<<0); 
	}
	public static int getARGB(java.awt.Color c)
	{
		return (c.getAlpha()<<24) | (c.getRed()<<16) | (c.getGreen()<<8) | (c.getBlue()<<0); 
	}
	@SuppressWarnings("unused")
	private static Color blend(Color... colorlist)
	{
		final boolean bitmask = true;
		long sum1=0,sum2=0;
		int alpha=0;
		int size=colorlist.length;
		if(size<=0)
			return null;
		for (Color var : colorlist)
		{
			int color=getARGB(var);
			alpha+=(color<<24);
			if(var.getAlpha()>0)
			{
				sum1+= color    &0x00FF00FF;
				sum2+=(color>>8)&0x00FF00FF;
			}
			if(var.getAlpha()==0)
				size-=1;
		}
		alpha=bitmask ? ((alpha==0)?0:0xFF) : (alpha/size);
		int value=0;
		try{
			value|=(((sum1&0xFFFF)/(size))&0xFF);}catch(ArithmeticException ae){}try{
				value|=(((sum2&0xFFFF)/(size))&0xFF)<<8;}catch(ArithmeticException ae){}try{
					value|=((((sum1>>=16)&0xFFFF)/(size))&0xFF)<<16;}catch(ArithmeticException ae){}
				return new Color(value|=alpha<<24, true);
	}
	public static Color getColorForName(String name)
	{
		for(NamedColor color : colors)
			if(color.name.equalsIgnoreCase(name))
				return color;
		return null;
	}
	public String getName()
	{
		return this.name;
	}
	@Override
	public String toString()
	{
		String argb=""+Integer.toUnsignedString(this.getARGB(), 16);
		while(argb.length()<8)
			argb="0"+argb;
		return getClass().getName()+"[name=\""+getName()+"\", argb=0x"+argb+"]";
	}
	private static final Comparator<NamedColor> sortnameup = new Comparator<NamedColor>()
		{
			public int compare(NamedColor o1, NamedColor o2)
			{
				int c1=o1.getName().compareToIgnoreCase(o2.getName());
				return c1!=0?c1:((Integer)o1.getRGB()).compareTo(o2.getRGB());
			}
		},
		sortnamedown = new Comparator<NamedColor>()
		{
			public int compare(NamedColor o1, NamedColor o2)
			{
				int c1=o1.getName().compareToIgnoreCase(o2.getName());
				return -(c1!=0?c1:((Integer)o1.getRGB()).compareTo(o2.getRGB()));
			}
		};
	public static <T extends List<NamedColor>> T sortByNameAcending(T lst)
	{
		lst.sort(sortnameup);
		return lst;
	}
	public static <T extends List<NamedColor>> T sortByNameDecending(T lst)
	{
		lst.sort(sortnamedown);
		return lst;
	}
}
