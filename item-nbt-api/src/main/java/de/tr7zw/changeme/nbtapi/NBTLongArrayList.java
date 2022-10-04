package de.tr7zw.changeme.nbtapi;

import de.tr7zw.changeme.nbtapi.utils.nmsmappings.ClassWrapper;
import de.tr7zw.changeme.nbtapi.utils.nmsmappings.ReflectionMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Integer implementation for NBTLists
 * 
 * @author tr7zw
 *
 */
public class NBTLongArrayList extends NBTList<long[]> {

    private final NBTContainer tmpContainer;

	protected NBTLongArrayList(NBTCompound owner, String name, NBTType type, Object list) {
		super(owner, name, type, list);
		this.tmpContainer = new NBTContainer();
	}

	@Override
	protected Object asTag(long[] object) {
		try {
			Constructor<?> con = ClassWrapper.NMS_NBTTAGLONGARRAY.getClazz().getDeclaredConstructor(long[].class);
			con.setAccessible(true);
			return con.newInstance(object);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", e);
		}
	}

	@Override
	public long[] get(int index) {
		try {
			Object obj = ReflectionMethod.LIST_GET.run(listObject, index);
			ReflectionMethod.COMPOUND_SET.run(tmpContainer.getCompound(), "tmp", obj);
			long[] val = tmpContainer.getLongArray("tmp");
			tmpContainer.removeKey("tmp");
			return val;
		} catch (NumberFormatException nf) {
			return null;
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

}
