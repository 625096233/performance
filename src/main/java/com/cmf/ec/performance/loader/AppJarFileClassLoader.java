package com.cmf.ec.performance.loader;

import java.io.File;

public class AppJarFileClassLoader extends JarFileClassLoader {
	private String[] ignorePackagePreffix = new String[] { "com.cmf.ec.performance", "java.", "com.sun", "javax.", "sun.", "sunw." };

	public AppJarFileClassLoader(File[] files) {
		super(files);
	}

	private boolean isIgnoreClass(String name) {
		for (String preffix : ignorePackagePreffix) {
			if (name.startsWith(preffix)) {
				return true;
			}
		}
		return false;
	}

	// XXX:�������˫��ί�У�����ʹ���û���jar������ô��֤ϵͳjar�������ǣ�����ֻ�Ǽ򵥸��ݰ�ǰ׺�ÿ��Spi�ӿڽ�����������ȥload�������Լ�ȥload���Ա���ת��ʧ�ܵ�����
	// ��֮spi�ӿں�ϵͳ�����class��������AppJarFileClassLoader�Լ�load��������Χ������ת��ʱ��ʧ��
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> c = findLoadedClass(name);
		ClassNotFoundException ex = null;
		if (c == null) {
			if (isIgnoreClass(name)) {
				try {
					c = getParent().loadClass(name);
				} catch (ClassNotFoundException e) {
					ex = e;
				}
			}
		}

		if (c == null) {
			try {
				c = this.findClass(name);
			} catch (ClassNotFoundException e) {
				ex = e;
			}
		}
		if (c == null) {
			try {
				c = getParent().loadClass(name);
			} catch (ClassNotFoundException e) {
				ex = e;
			}
		}
		if (c == null) {
			throw ex;
		}
		return c;
	}

}
